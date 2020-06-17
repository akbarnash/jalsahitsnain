package app.com.jalsahitsnain.activity.news;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.com.jalsahitsnain.R;

public class DetailNews extends AppCompatActivity {

   ImageView main,shortimg;
   TextView titlee, dates;
   HtmlTextView discc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Berita");

        String Title = getIntent().getStringExtra("title");
        String disc =  getIntent().getStringExtra("disc");
        String imageUrl = getIntent().getStringExtra("Image");
        String Date = getIntent().getStringExtra("date");
        Date = formatDate(Date);

        main = findViewById(R.id.imageWebPage);
        shortimg = findViewById(R.id.mainImage);
        titlee = findViewById(R.id.mainTitle);
        dates = findViewById(R.id.mainDate);
        discc = findViewById(R.id.mainDisc);


        /*Picasso.get().load(imageUrl).into(main);
        Picasso.get().load(imageUrl).into(shortimg);*/
        Picasso.with(this).load(imageUrl).into(shortimg);

        //Glide.with(this).load(imageUrl).into(shortimg);

        //Document document = Jsoup.parse(disc);

        titlee.setText(Title);
        dates.setText(Date);
        discc.setHtml(disc, new HtmlHttpImageGetter(discc));
        //discc.setText(Html.fromHtml(document.text()));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
