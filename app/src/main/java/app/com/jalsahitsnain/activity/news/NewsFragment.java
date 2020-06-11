package app.com.jalsahitsnain.activity.news;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.activity.DetailLiveAudio;
import app.com.jalsahitsnain.fragments.VideoFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private RecyclerView.Adapter adapters;
    private RecyclerView recyclerView;
    private List<Posts> newsList = new ArrayList<>();
    private String URL_DATA;
    private RequestQueue reqQue;
    private  long id;
    private ProgressBar progressBar;
    CarouselView carouselView;
    SwipeRefreshLayout swipeRefreshLayout;
    int[] sampleImages = {R.drawable.images_3, R.drawable.images_4, R.drawable.images_2, R.drawable.images_1};


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        URL_DATA = "https://jalsahitsnainkng.000webhostapp.com/wordpress/wp-json/wp/v2/posts/";

        swipeRefreshLayout = rootView.findViewById(R.id.swNews);

        carouselView = rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        progressBar = rootView.findViewById(R.id.pgNews);
        recyclerView = rootView.findViewById(R.id.recycles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapters = new NewsAdapter(newsList, getActivity());


        recyclerView.setAdapter(adapters);
        SetUrl();
        loadUrl();

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsList.clear();

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swipeRefreshLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        loadUrl();
                    }
                }, 2000);
            }
        });

        return rootView;


    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    };

    private void loadUrl() {
        JsonArrayRequest stringRequest = new JsonArrayRequest(URL_DATA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                getValue(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        reqQue = Volley.newRequestQueue(getActivity());
        reqQue.add(stringRequest);


    }

    public void getValue(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            Posts userList = new Posts();

            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                // JsonObject obj  = array.getJSONObject(i);
                /*JSONObject links = json.getJSONObject("_links");
                JSONArray wpfeaturedimg = links.getJSONArray("wp:featuredmedia");
                for (int a = 0; a < wpfeaturedimg.length(); a++){
                    JSONObject href = wpfeaturedimg.getJSONObject(a);
                    userList.setPostImg(href.getString("href"));
                }*/

                userList.setCreatedAt(json.getString("date"));

                userList.setPostImg(json.getString("jetpack_featured_media_url"));
                userList.setPostURL(json.getString("link"));
                JSONObject titlee = json.getJSONObject("title");
                userList.setTitle(titlee.getString("rendered"));

                JSONObject disc = json.getJSONObject("content");
                userList.setDescription(disc.getString("rendered"));







            } catch (Exception e) {
            }
            newsList.add(userList);
        }


        adapters = new NewsAdapter(newsList, getActivity());
        recyclerView.setAdapter(adapters);

        progressBar.setVisibility(View.GONE);

    }

    public void SetUrl() {

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
        {
            id = (long) getActivity().getIntent().getLongExtra("idss",410);
            URL_DATA = "https://jalsahitsnainkng.000webhostapp.com/wordpress/wp-json/wp/v2/posts?categories=" + id;
        }





    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}