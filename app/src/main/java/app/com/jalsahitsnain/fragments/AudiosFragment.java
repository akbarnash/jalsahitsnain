package app.com.jalsahitsnain.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.models.SongModel;
import app.com.jalsahitsnain.activity.DetailLiveAudio;

public class AudiosFragment extends Fragment {
    ListView listView;

    ArrayList<String> arrayListSongsName = new ArrayList<>();
    ArrayList<String> arrayListSongsUrl = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    public AudiosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audios, container, false);

        listView = view.findViewById(R.id.myListView);
        jcPlayerView = view.findViewById(R.id.jcplayer);
        progressBar = view.findViewById(R.id.pgAudios);
        swipeRefreshLayout = view.findViewById(R.id.swAudio);

        retrieveSongs();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification(R.drawable.logo);
            }
        });

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jcAudios.clear();
                arrayListSongsName.clear();
                arrayListSongsUrl.clear();

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swipeRefreshLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        retrieveSongs();
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void retrieveSongs() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("song");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    SongModel songObj = ds.getValue(SongModel.class);
                    arrayListSongsName.add(songObj.getSongTitle());
                    arrayListSongsUrl.add(songObj.getSongLink());
                    jcAudios.add(JcAudio.createFromURL(songObj.getSongTitle(), songObj.getSongLink()));
                }
                arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.song_item,arrayListSongsName){

                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView)view.findViewById(R.id.songtitle);
                        //Drawable img = getContext().getResources().getDrawable(R.drawable.music);

                        textView.setSingleLine(true);
                        //textView.setCompoundDrawables(img, null, null, null);
                        textView.setMaxLines(1);

                        return super.getView(position, convertView, parent);
                    }
                };
                jcPlayerView.initPlaylist(jcAudios, null);
                listView.setAdapter(arrayAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d(TAG, databaseError.getMessage());
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.live_aud_menu, menu);
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem livVid = menu.findItem(R.id.live_aud);
        livVid.getIcon().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        livVid.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), DetailLiveAudio.class);
                startActivity(intent);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.live_aud:
                Intent intent = new Intent(getActivity(), DetailLiveAudio.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
