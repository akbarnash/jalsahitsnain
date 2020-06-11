package app.com.jalsahitsnain.activity.wpnews;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.jalsahitsnain.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<PostModel> list;
    private ArrayList<PostMedia> mediaList;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    public Parcelable recyclerViewState;

    private int postsPerPage = 10;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.images_3, R.drawable.images_4, R.drawable.images_2, R.drawable.images_1};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_home);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        loadData();
        startAsyncDataTask(postsPerPage);

        adapter = new PostAdapter(list, mediaList, getContext());
        recyclerView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    postsPerPage = postsPerPage + 10;
                    startAsyncDataTask(postsPerPage);

                }
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop animation (This will be after 1 seconds)
//
//                list.clear();
//                adapter.clearModel();
//
//                mediaList.clear();
//                adapter.clearPostMediaList();
                startAsyncDataTask(postsPerPage);
//                getRetrofitData();
//                swipeContainer.setRefreshing(false);
            }
        }, 1000); // Delay in millis
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("HOME_TASK_LIST", null);
        Type type = new TypeToken<ArrayList<PostModel>>() {
        }.getType();
        list = gson.fromJson(json, type);

        Gson gson2 = new Gson();
        String json2 = sharedPreferences.getString("HOME_TASK_IMAGE_LIST", null);
        Type type2 = new TypeToken<ArrayList<PostMedia>>() {
        }.getType();
        mediaList = gson2.fromJson(json2, type2);

        if (list == null) {
            list = new ArrayList<PostModel>();
        }
        if (mediaList == null) {
            mediaList = new ArrayList<PostMedia>();
        }
        if (json == null || json2 == null) {
            startAsyncDataTask(postsPerPage);
        }
    }

    private void startAsyncDataTask(final Integer postsPerPage) {
        final getRetrofitDataAsyncTask task = new getRetrofitDataAsyncTask(this);
        task.execute(postsPerPage);
    }

    private static String formatDate(String mDate) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sd.parse(mDate);
            sd = new SimpleDateFormat("dd MMM yyyy");
            return sd.format(d);
        } catch (ParseException e) {
        }
        return "";
    }

    private static class getRetrofitDataAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private WeakReference<PostFragment> fragmentWeakReference;

        private boolean isComplete = false;

        getRetrofitDataAsyncTask(PostFragment postFragment) {
            fragmentWeakReference = new WeakReference<PostFragment>(postFragment);
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {

            final PostFragment postFragment = fragmentWeakReference.get();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(postFragment.getResources().getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
            Call<List<WPPost>> call = service.getPostPerPage(integers[0]);

            call.enqueue(new Callback<List<WPPost>>() {
                @Override
                public void onResponse(Call<List<WPPost>> call, Response<List<WPPost>> response) {
                    Log.e(TAG, "onResponse: " + response.body());

                    postFragment.adapter.clearModel();

                    for (int i = 0; i < response.body().size(); i++) {
                        int mId = response.body().get(i).getId();
                        Log.d(TAG, "onResponseID: " + mId);
                        String mediaUrl = response.body().get(i).getLinks().getWpAttachment().get(0).getHref();
                        String mTitle = response.body().get(i).getTitle().getRendered();
                        String mSubtitle = response.body().get(i).getExcerpt().getRendered();
                        String mDate = response.body().get(i).getDate();
                        //DateFormat.getDateInstance().format(mDate);
                        mDate = formatDate(mDate);

                        mSubtitle = mSubtitle.replace("<p>", "");
                        mSubtitle = mSubtitle.replace("</p>", "");
                        mSubtitle = mSubtitle.replace("[&hellip;]", "");

                        String mContent = response.body().get(i).getContent().getRendered();

                        Log.d(TAG, "onResponse: "
                                + "\n================================================================================================================================================================================================================================================"
                                + "\nid: \t\t" + mId
                                + "\nTitle: \t\t" + mTitle
                                + "\nDate: \t\t" + mDate
                                + "\nSubtitle: \t" + mSubtitle
                                + "\nContent: \t\t" + mContent
                                + "\n================================================================================================================================================================================================================================================");

                        postFragment.list.add(new PostModel(PostModel.IMAGE_TYPE, mediaUrl, mId, mTitle, mSubtitle, mContent, mDate));
                        postFragment.getRetrofitImage(mediaUrl);
                        postFragment.saveDataList();
                    }
                    isComplete = true;
                    postFragment.adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<WPPost>> call, Throwable t) {

                }
            });
            return isComplete;
        }

        @Override
        protected void onPreExecute() {
            PostFragment homeFragment = fragmentWeakReference.get();
            if (homeFragment == null || homeFragment.isDetached()) {
                return;
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isComplete) {
            super.onPostExecute(isComplete);
            PostFragment homeFragment = fragmentWeakReference.get();
            if (homeFragment == null || homeFragment.isDetached()) {
                return;
            }
            homeFragment.swipeContainer.setRefreshing(false);
        }
    }

    private void getRetrofitImage(final String mediaUrl) {

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service2 = retrofit2.create(RetrofitArrayApi.class);

        adapter.clearPostMediaList();

        Call<List<WPPostID>> call2 = service2.getWpAttachment(mediaUrl);
        Log.d(TAG, "getRetrofitImageMediaUrl: " + mediaUrl);
        call2.enqueue(new Callback<List<WPPostID>>() {
                          @Override
                          public void onResponse(Call<List<WPPostID>> call, Response<List<WPPostID>> response) {

                              Log.e(TAG, "onResponse: " + response.body());
                              Log.d(TAG, "onResponse: mediaUrl: " + mediaUrl);

                              if (response.body().size() != 0) {
                                  String mediaUrl = response.body().get(0).getMediaDetails().getSizes().getThumbnail().getSourceUrl();
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t with media " + mediaUrl + "\n******************************");
                              } else {
                                  String mediaUrl = "NOIMAGE";
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t null media\n" + mediaUrl + "\n******************************");
                              }
                              adapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onFailure(Call<List<WPPostID>> call, Throwable t) {
                          }
                      }
        );
    }

    public void saveDataList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("HOME_TASK_LIST", json);

        editor.apply();
    }

    public void saveDataImageList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson2 = new Gson();
        String json2 = gson2.toJson(mediaList);
        editor.putString("HOME_TASK_IMAGE_LIST", json2);

        editor.apply();
    }


}
