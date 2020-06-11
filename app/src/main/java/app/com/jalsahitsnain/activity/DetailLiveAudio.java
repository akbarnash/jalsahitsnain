package app.com.jalsahitsnain.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.util.Server;

public class DetailLiveAudio extends AppCompatActivity {

    String ip, port;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_audio);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Audio Streaming");

        jcPlayerView = findViewById(R.id.jcplayer);
        jsonParse();

    }

    private void jsonParse() {
        AndroidNetworking.get(Server.URL + "audlink.php")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(@NotNull JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int a = 0; a < jsonArray.length(); a ++){
                                JSONObject json = jsonArray.getJSONObject(a);
                                ip = json.getString("ip");
                                port = json.getString("port");
                            }

                            jcAudios.add(JcAudio.createFromURL("Live Audio Jalsah Itsnain", "http://"+ip+":"+port));
                            jcPlayerView.initPlaylist(jcAudios, null);

                            jcPlayerView.playAudio(jcAudios.get(0));
                            jcPlayerView.createNotification(R.drawable.logo);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
