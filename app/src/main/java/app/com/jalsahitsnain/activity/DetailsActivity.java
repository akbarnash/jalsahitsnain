package app.com.jalsahitsnain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.models.YoutubeDataModel;
import app.com.jalsahitsnain.util.Server;

public class DetailsActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static String YOUTUBE_API = Server.API_KEY;

    private YoutubeDataModel youtubeDataModel = null;
    TextView textViewName,textViewDes,textViewDate,vid_id;
    TextView tvTitle1, tvTitle2, tvTitle3,tvTitle4,txtrec1,txtrec2,txtrec3,txtrec4;
    ImageView imgThumb1, imgThumb2, imgThumb3,imgThumb4;
    LinearLayout lt1,lt2,lt3,lt4,content;
    private YouTubePlayerView mYoutubePlayerView = null;
    private YouTubePlayer mYoutubePlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        youtubeDataModel = getIntent().getParcelableExtra(YoutubeDataModel.class.toString());
        Log.e("", youtubeDataModel.getDescription());

        mYoutubePlayerView = findViewById(R.id.youtube_player);
        mYoutubePlayerView.initialize(YOUTUBE_API, this);

        //layout video
        textViewName = findViewById(R.id.textViewName);
        textViewDes = findViewById(R.id.textViewDes);
        textViewDate = findViewById(R.id.textViewDate);
        vid_id = findViewById(R.id.vid_id);

        //layout rekomen
        lt1 = findViewById(R.id.t1);
        lt2 = findViewById(R.id.t2);
        lt3 = findViewById(R.id.t3);
        lt4 = findViewById(R.id.t4);
        content = findViewById(R.id.contentrec);

        //item rekomen
        imgThumb1 = findViewById(R.id.ImageThumb1);
        tvTitle1 = findViewById(R.id.textViewTitle1);
        txtrec1 = findViewById(R.id.txtrec1);
        imgThumb2 = findViewById(R.id.ImageThumb2);
        tvTitle2 = findViewById(R.id.textViewTitle2);
        txtrec2 = findViewById(R.id.txtrec2);
        imgThumb3 = findViewById(R.id.ImageThumb3);
        tvTitle3 = findViewById(R.id.textViewTitle3);
        txtrec3 = findViewById(R.id.txtrec3);
        imgThumb4 = findViewById(R.id.ImageThumb4);
        tvTitle4 = findViewById(R.id.textViewTitle4);
        txtrec4 = findViewById(R.id.txtrec4);

        textViewName.setText(youtubeDataModel.getTitle());
        textViewDes.setText(youtubeDataModel.getDescription());
        textViewDate.setText(youtubeDataModel.getPublishedAt());
        vid_id.setText(youtubeDataModel.getVideo_id());

        vid_id.setVisibility(View.INVISIBLE);

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
        //rule 1 => 2,3,4,5
        if (vid_id.getText().equals("Ukc9DDqR2kU")) {
            vid_id.setVisibility(View.GONE);

            //id 2
            imgThumb1.setImageResource(R.drawable.thumb2);
            tvTitle1.setText(R.string.judul2);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul2));
                    i.putExtra("link", "0HJ6bAFirPw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 3
            imgThumb2.setImageResource(R.drawable.thumb3);
            tvTitle2.setText(R.string.judul3);
            lt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul3));
                    i.putExtra("link", "DKwPDv50Hsw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 4
            imgThumb3.setImageResource(R.drawable.thumb4);
            tvTitle3.setText(R.string.judul4);
            lt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul4));
                    i.putExtra("link", "wzpP0KDXc-E");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 5
            imgThumb4.setImageResource(R.drawable.thumb5);
            tvTitle4.setText(R.string.judul5);
            lt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul5));
                    i.putExtra("link", "0PEAR2QSb54");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

        }

        //rule 2,3,5 => 1,4
        else if (vid_id.getText().equals("0HJ6bAFirPw")||vid_id.getText().equals("DKwPDv50Hsw")||vid_id.getText().equals("0PEAR2QSb54")) {
            vid_id.setVisibility(View.GONE);

            //id 1
            imgThumb1.setImageResource(R.drawable.thumb1);
            tvTitle1.setText(R.string.judul1);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul1));
                    i.putExtra("link", "Ukc9DDqR2kU");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 2
            imgThumb2.setImageResource(R.drawable.thumb2);
            tvTitle2.setText(R.string.judul2);
            lt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul2));
                    i.putExtra("link", "0HJ6bAFirPw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            lt3.setVisibility(View.GONE);
            lt4.setVisibility(View.GONE);

        }

        //rule 4 => 1,2,3,5
        else if (vid_id.getText().equals("wzpP0KDXc-E")){
            vid_id.setVisibility(View.GONE);

            //id 1
            imgThumb1.setImageResource(R.drawable.thumb1);
            tvTitle1.setText(R.string.judul1);
            lt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul1));
                    i.putExtra("link", "Ukc9DDqR2kU");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 2
            imgThumb2.setImageResource(R.drawable.thumb2);
            tvTitle2.setText(R.string.judul2);
            lt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul2));
                    i.putExtra("link", "0HJ6bAFirPw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 3
            imgThumb3.setImageResource(R.drawable.thumb3);
            tvTitle3.setText(R.string.judul3);
            lt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul3));
                    i.putExtra("link", "DKwPDv50Hsw");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

            //id 5
            imgThumb4.setImageResource(R.drawable.thumb5);
            tvTitle4.setText(R.string.judul5);
            lt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsActivity.this, DetailsActivity2.class);
                    i.putExtra("title", getString(R.string.judul5));
                    i.putExtra("link", "0PEAR2QSb54");
                    startActivity(i);
                    DetailsActivity.this.finish();
                }
            });

        }

        else{
            vid_id.setVisibility(View.VISIBLE);
            vid_id.setText("BELUM ADA REKOMENDASI");
            lt1.setVisibility(View.GONE);
            lt2.setVisibility(View.GONE);
            lt3.setVisibility(View.GONE);
            lt4.setVisibility(View.GONE);
        }
    }

}
