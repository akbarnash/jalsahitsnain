package app.com.jalsahitsnain.activity.news;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.com.jalsahitsnain.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolde> {
 List<Posts> data;
 Context context;


 public NewsAdapter(List<Posts> data , Context context){
     this.context = context;
     this.data = data;

 }

    @NonNull
    @Override
    public NewsViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item,parent,false);


        return new NewsViewHolde(v);
    }

    private static String formatDate(String createdAt) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sd.parse(createdAt);
            sd = new SimpleDateFormat("EEE, dd MMM yyyy");
            return sd.format(d);
        } catch (ParseException e) {
        }
        return "";
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolde holder, int position) {
                Posts news = data.get(position);

        Document document = Jsoup.parse(news.getDescription());
                holder.Title.setText(news.getTitle());
                holder.Disc.setText(document.text());
                String createdAt = news.getCreatedAt();
                createdAt = formatDate(createdAt);
                holder.Date.setText(createdAt);


       // Picasso.get().load(news.getPostURL()).into(holder.image);
        Glide.with(context).load(news.postImg).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NewsViewHolde extends RecyclerView.ViewHolder {
     TextView Title,Disc, Date;
        ImageView image;


     public NewsViewHolde(@NonNull View itemView) {
            super(itemView);

            Disc = itemView.findViewById(R.id.discription);
            Title = itemView.findViewById(R.id.titless);
            image = itemView.findViewById(R.id.imagess);
            Date = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Posts newsData = data.get(getAdapterPosition());
                    Intent i = new Intent(context, DetailNews.class);
                    i.putExtra("URL",newsData.getPostURL());
                    i.putExtra("Image",newsData.getPostImg());
                    i.putExtra("disc",newsData.getDescription());
                    i.putExtra("title",newsData.getTitle());
                    i.putExtra("date",newsData.getCreatedAt());
                   // i.putExtra()


                    context.startActivity(i);
                }
            });

        }
    }
}
