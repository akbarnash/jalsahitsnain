package app.com.jalsahitsnain.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import app.com.jalsahitsnain.activity.DetailLiveVideo;
import app.com.jalsahitsnain.activity.DetailsActivity;
import app.com.jalsahitsnain.adapters.VideoPostAdapter;
import app.com.jalsahitsnain.interfaces.OnItemClickListener;
import app.com.jalsahitsnain.models.YoutubeDataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDzgdvUwCUmLV4fOUfrkRkVFbto1ur6_Us"; //here you should use your api key for testing purpose you can use this api also
    private static String PLAYLIST_ID = "PL9jIWytrbwrxxbfzUJLCHI4v4sWP2JJOj"; //here you should use your playlist id for testing purpose you can use this api also
    private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + PLAYLIST_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";

    private RecyclerView mList_videos = null;
    private VideoPostAdapter adapter = null;
    private ArrayList < YoutubeDataModel > mListData = new ArrayList < > ();

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    SwipeRefreshLayout swLayout;
    ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mList_videos = view.findViewById(R.id.mList_videos);
        swLayout = view.findViewById(R.id.swlayout);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        progressBar = view.findViewById(R.id.pgVideo);

        initList(mListData);
        new RequestYoutubeAPI().execute();

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListData.clear();

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        initList(mListData);
                        new RequestYoutubeAPI().execute();
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void initList(ArrayList < YoutubeDataModel > mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
            }
        });

        mList_videos.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

    }

    //create an asynctask to get all the data from youtube
    public class RequestYoutubeAPI extends AsyncTask < Void, String, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void...params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
            Log.e("URL", CHANNLE_GET_URL);
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

    public ArrayList < YoutubeDataModel > parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList < YoutubeDataModel > mList = new ArrayList < > ();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("kind")) {
                        if (json.getString("kind").equals("youtube#playlistItem")) {
                            YoutubeDataModel youtubeObject = new YoutubeDataModel();
                            JSONObject jsonSnippet = json.getJSONObject("snippet");
                            String vedio_id = "";
                            if (jsonSnippet.has("resourceId")) {
                                JSONObject jsonResource = jsonSnippet.getJSONObject("resourceId");
                                vedio_id = jsonResource.getString("videoId");
                            }
                            String title = jsonSnippet.getString("title");
                            String description = jsonSnippet.getString("description");
                            String publishedAt = jsonSnippet.getString("publishedAt");
                            String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                            youtubeObject.setTitle(title);
                            youtubeObject.setDescription(description);
                            youtubeObject.setPublishedAt(publishedAt);
                            youtubeObject.setThumbnail(thumbnail);
                            youtubeObject.setVideo_id(vedio_id);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar, menu);
        inflater.inflate(R.menu.live_vid_menu, menu);
        inflater.inflate(R.menu.option_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem livVid = menu.findItem(R.id.live_vid);
        livVid.getIcon().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        livVid.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), DetailLiveVideo.class);
                startActivity(intent);
                return true;
            }
        });

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Cari video...");

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                    query = query.toLowerCase();
                    ArrayList < YoutubeDataModel > dataFilter = new ArrayList < > ();
                    for (YoutubeDataModel data: mListData) {
                        String nama = data.getTitle().toLowerCase();
                        if (nama.contains(query)) {
                            dataFilter.add(data);
                        }
                    }
                    if(dataFilter.isEmpty()){
                        Toast.makeText(getContext(),"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        // Handler untuk menjalankan jeda selama 5 detik
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {

                                // Berhenti berputar/refreshing
                                swLayout.setRefreshing(false);

                                // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                                initList(mListData);
                                new RequestYoutubeAPI().execute();
                            }
                        }, 5000);
                    }
                    adapter.setFilter(dataFilter);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;

            case R.id.live_vid:
                Intent intent = new Intent(getActivity(), DetailLiveVideo.class);
                startActivity(intent);
                return true;

            default:
                break;
        }

        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

}