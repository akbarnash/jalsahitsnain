package app.com.jalsahitsnain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.util.Server;

public class DetailLiveVideo  extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    private static String YOUTUBE_API = Server.API_KEY;
    public String links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live_video);

        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API, this);

        jsonParse();

    }

    private void jsonParse() {
        AndroidNetworking.get(Server.URL + "ytlink.php")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(@NotNull JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int a = 0; a < jsonArray.length(); a ++){
                                JSONObject json = jsonArray.getJSONObject(a);
                                links = json.getString("link");
                            }
                            //Toast.makeText(getApplicationContext(), links, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        Toast.makeText(getApplicationContext(), "Sedang memuat streaming....", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (!wasRestored) {
                    player.loadVideo(links);
                }
            }
        }, 2000);

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

}

