package app.com.jalsahitsnain.activity.wpnews;

import android.content.Context;
import android.content.Intent;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.com.jalsahitsnain.R;

public class PostAdapter extends RecyclerView.Adapter {

    private ArrayList<PostModel> postDataset;
    private ArrayList<PostMedia> imageDataset;
    private Context mContext;

    public void clearModel() {
        int sizeDa = postDataset.size();
        postDataset.clear();
        notifyItemRangeRemoved(0, sizeDa);
    }

    public void clearPostMediaList() {
        int sizeIm = imageDataset.size();
        imageDataset.clear();
        notifyItemRangeRemoved(0, sizeIm);
    }

    public void addAll(ArrayList<PostModel> list, ArrayList<PostMedia> imageList) {
        postDataset.addAll(list);
        imageDataset.addAll(imageList);
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView title, excerpt, date;
        ImageView imageView;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.post_title);
            this.excerpt = itemView.findViewById(R.id.post_excerpt);
            this.imageView = itemView.findViewById(R.id.post_img);
            this.date = itemView.findViewById(R.id.post_date);
        }
    }

    public PostAdapter(ArrayList<PostModel> mlist, ArrayList<PostMedia> mMediaList, Context context) {
        this.postDataset = mlist;
        this.imageDataset = mMediaList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_post_layout, viewGroup, false);
        return new ImageTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);

         final PostModel object = postDataset.get(position);
         final PostMedia object2 = imageDataset.get(position);

        String postMediaUrl = object2.PostMediaUrl;

        if (postMediaUrl == "NOIMAGE") {
            ((ImageTypeViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            ((ImageTypeViewHolder) holder).imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        } else {
            ((ImageTypeViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(postMediaUrl)
                    .into(((ImageTypeViewHolder) holder).imageView);
        }

        ((ImageTypeViewHolder) holder).title.setText(object.title);
        ((ImageTypeViewHolder) holder).excerpt.setText(object.excerpt);
        ((ImageTypeViewHolder) holder).date.setText(object.date);

        ((ImageTypeViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.date, object.content);
            }
        });
        ((ImageTypeViewHolder) holder).title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.date, object.content);
            }
        });
        ((ImageTypeViewHolder) holder).excerpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.date, object.content);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

    }

    public void startDetailPostFragment(String title, String date, String content) {
        Bundle bundle = new Bundle();
        bundle.putString("POST_TITLE", title);
        bundle.putString("POST_DATE", date);
        bundle.putString("POST_CONTENT", content);
        Intent intent = new Intent(mContext, PostDetails.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        /*Intent intent = new Intent("POST_DATA");
        intent (mContext, PostDetails.class);
        intent.putExtra("POST_TITLE", title);
        intent.putExtra("POST_CONTENT", content);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);*/
        //mContext.startActivity(PostDetails);
        //startActivity(intent);
        /*FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle postDetailFragmentBundle = new Bundle();
        postDetailFragmentBundle.putString("POST_TITLE", title);
        postDetailFragmentBundle.putString("POST_CONTENT", content);
        postDetailFragment.setArguments(postDetailFragmentBundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, postDetailFragment).addToBackStack(null).commit();*/
    }

    @Override
    public int getItemCount() {
        return imageDataset.size();
    }
}
