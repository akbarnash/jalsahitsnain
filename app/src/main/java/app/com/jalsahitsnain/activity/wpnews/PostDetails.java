package app.com.jalsahitsnain.activity.wpnews;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import app.com.jalsahitsnain.R;

public class PostDetails extends AppCompatActivity {
    private TextView titleTV, dateTV;
    private HtmlTextView contentTV;
    private String title, content, date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Berita");

        titleTV = findViewById(R.id.post_det_title);
        dateTV = findViewById(R.id.post_det_date);
        contentTV = findViewById(R.id.post_det_content);

        title = getIntent().getStringExtra("POST_TITLE");
        date = getIntent().getStringExtra("POST_DATE");
        content = getIntent().getStringExtra("POST_CONTENT");

        titleTV.setText(title);
        dateTV.setText(date);

        contentTV.setHtml(content,new HtmlHttpImageGetter(contentTV));

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
