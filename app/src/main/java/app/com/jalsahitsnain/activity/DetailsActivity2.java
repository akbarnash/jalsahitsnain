package app.com.jalsahitsnain.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.adapters.VideoPostAdapter2;
import app.com.jalsahitsnain.interfaces.OnItemClickListener;
import app.com.jalsahitsnain.models.YoutubeDataModel;
import app.com.jalsahitsnain.util.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsActivity2 extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static String YOUTUBE_API = Server.API_KEY;
    private static String CHANNEL_GET_URL = Server.API_URL;

    private RecyclerView mList_videos = null;
    private VideoPostAdapter2 adapter = null;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    String link = null;
    TextView textViewName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API, this);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        link = intent.getStringExtra("link");

        mList_videos = findViewById(R.id.mList_videos);
        textViewName = findViewById(R.id.textViewName);

        textViewName.setText(title);

        initList(mListData);
        new RequestYoutubeAPI().execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(link);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoPostAdapter2(this, mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(DetailsActivity2.this, DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
                DetailsActivity2.this.finish();
            }
        });
        mList_videos.setAdapter(adapter);
    }

    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNEL_GET_URL);
            Log.e("URL", CHANNEL_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("kind")) {
                        if (json.getString("kind").equals("youtube#playlistItem")) {
                            YoutubeDataModel youtubeObject = new YoutubeDataModel();
                            JSONObject jsonSnippet = json.getJSONObject("snippet");
                            String video_id = "";
                            if (jsonSnippet.has("resourceId")) {
                                JSONObject jsonResource = jsonSnippet.getJSONObject("resourceId");
                                video_id = jsonResource.getString("videoId");
                            }
                            String title = jsonSnippet.getString("title");
                            String description = jsonSnippet.getString("description");
                            String publishedAt = jsonSnippet.getString("publishedAt");
                            String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                            youtubeObject.setTitle(title);
                            youtubeObject.setDescription(description);
                            youtubeObject.setPublishedAt(publishedAt);
                            youtubeObject.setThumbnail(thumbnail);
                            youtubeObject.setVideo_id(video_id);
                            mList.add(youtubeObject);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }
}

