package app.com.jalsahitsnain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.models.YoutubeDataModel;

public class DetailsActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static String GOOGLE_YOUTUBE_API = "AIzaSyDzgdvUwCUmLV4fOUfrkRkVFbto1ur6_Us";
    private YoutubeDataModel youtubeDataModel = null;
    TextView textViewName,textViewDes,textViewDate,vid_id;
    TextView textViewTitle1,textViewTitle2,textViewTitle3,txtrec1,txtrec2,txtrec3;
    ImageView imageThumb1,imageThumb2,imageThumb3;
    LinearLayout lt1,lt2,lt3,content;
    Button desc, rec;
    private YouTubePlayerView mYoutubePlayerView = null;
    private YouTubePlayer mYoutubePlayer = null;
    RecyclerView mList_videos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        youtubeDataModel = getIntent().getParcelableExtra(YoutubeDataModel.class.toString());
        Log.e("", youtubeDataModel.getDescription());

        mYoutubePlayerView = findViewById(R.id.youtube_player);
        mYoutubePlayerView.initialize(GOOGLE_YOUTUBE_API, this);

        mList_videos = findViewById(R.id.mList_videos);

        textViewName = findViewById(R.id.textViewName);
        textViewDes = findViewById(R.id.textViewDes);
        textViewDes.setMovementMethod(new ScrollingMovementMethod());
        textViewDate = findViewById(R.id.textViewDate);
        vid_id = findViewById(R.id.vid_id);
        mList_videos = findViewById(R.id.mList_videos);

        lt1 = findViewById(R.id.t1);
        lt2 = findViewById(R.id.t2);
        lt3 = findViewById(R.id.t3);
        content = findViewById(R.id.contentrec);

        imageThumb1 = findViewById(R.id.ImageThumb1);
        textViewTitle1 = findViewById(R.id.textViewTitle1);
        txtrec1 = findViewById(R.id.txtrec1);
        imageThumb2 = findViewById(R.id.ImageThumb2);
        textViewTitle2 = findViewById(R.id.textViewTitle2);
        txtrec2 = findViewById(R.id.txtrec2);
        imageThumb3 = findViewById(R.id.ImageThumb3);
        textViewTitle3 = findViewById(R.id.textViewTitle3);
        txtrec3 = findViewById(R.id.txtrec3);

        desc = findViewById(R.id.show_desc);
        rec = findViewById(R.id.show_rec);

        textViewName.setText(youtubeDataModel.getTitle());
        textViewDes.setText(youtubeDataModel.getDescription());
        textViewDate.setText(youtubeDataModel.getPublishedAt());
        vid_id.setText(youtubeDataModel.getVideo_id());

        vid_id.setVisibility(View.INVISIBLE);

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDes.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                vid_id.setVisibility(View.GONE);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setVisibility(View.VISIBLE);
                if (vid_id.getText().equals("BELUM ADA REKOMENDASI")){
                    vid_id.setVisibility(View.VISIBLE);
                }
                textViewDes.setVisibility(View.GONE);
            }
        });
        rekomen();
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            youTubePlayer.loadVideo(youtubeDataModel.getVideo_id());
        }
        mYoutubePlayer = youTubePlayer;
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {}
        @Override
        public void onPaused() {}
        @Override
        public void onStopped() {}
        @Override
        public void onBuffering(boolean b) {}
        @Override
        public void onSeekTo(int i) {}
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener =
            new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {}
        @Override
        public void onLoaded(String s) {}
        @Override
        public void onAdStarted() {}
        @Override
        public void onVideoStarted() {}
        @Override
        public void onVideoEnded() {}
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {}
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult
            youTubeInitializationResult) {

    }

    public void rekomen(){
        if (vid_id.getText().equals("Ukc9DDqR2kU")) {
            vid_id.setVisibility(View.GONE);
            imageThumb1.setImageResource(R.drawable.thumb2);
            textViewTitle1.setText(R.string.judul1);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul1));
                    i.putExtra("link", "0HJ6bAFirPw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            imageThumb2.setImageResource(R.drawable.thumb5);
            textViewTitle2.setText(R.string.judul2);
            lt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul2));
                    i.putExtra("link", "0PEAR2QSb54");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            imageThumb3.setVisibility(View.GONE);
            textViewTitle3.setVisibility(View.GONE);
            txtrec3.setVisibility(View.GONE);

        }
        else if (vid_id.getText().equals("wzpP0KDXc-E")) {
            vid_id.setVisibility(View.GONE);
            imageThumb1.setImageResource(R.drawable.thumb2);
            textViewTitle1.setText(R.string.judul1);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul1));
                    i.putExtra("link", "0HJ6bAFirPw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            imageThumb2.setImageResource(R.drawable.thumb5);
            textViewTitle2.setText(R.string.judul2);
            lt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul2));
                    i.putExtra("link", "0PEAR2QSb54");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            imageThumb3.setImageResource(R.drawable.thumb1);
            textViewTitle3.setText(R.string.judul3);
            lt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul3));
                    i.putExtra("link", "Ukc9DDqR2kU");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

        }
        else if (vid_id.getText().equals("DKwPDv50Hsw")||vid_id.getText().equals("0PEAR2QSb54")||vid_id.getText().equals("kzn815plYxU")){
            vid_id.setVisibility(View.GONE);
            imageThumb1.setImageResource(R.drawable.thumb2);
            textViewTitle1.setText(R.string.judul3);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul3));
                    i.putExtra("link", "Ukc9DDqR2kU");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            imageThumb2.setVisibility(View.GONE);
            textViewTitle2.setVisibility(View.GONE);
            txtrec2.setVisibility(View.GONE);
            imageThumb3.setVisibility(View.GONE);
            textViewTitle3.setVisibility(View.GONE);
            txtrec3.setVisibility(View.GONE);
        }
        else{
            vid_id.setText("BELUM ADA REKOMENDASI");
            lt1.setVisibility(View.GONE);
            lt2.setVisibility(View.GONE);
            lt3.setVisibility(View.GONE);
        }
    }

}
