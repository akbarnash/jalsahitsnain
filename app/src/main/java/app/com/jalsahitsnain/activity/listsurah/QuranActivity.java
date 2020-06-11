package app.com.jalsahitsnain.activity.listsurah;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.activity.listayat.ListAyatActivity;
import app.com.jalsahitsnain.base.BaseActivity;
import app.com.jalsahitsnain.models.modelquran.Surah;

public class QuranActivity extends BaseActivity<ListSurahPresenter> implements ListSurahView, ListSurahAdapter.OnSurahItemClick {


    @BindView(R.id.list_surah)
    RecyclerView recyclerView;
    private ListSurahAdapter listSurahAdapter;
    private String loadTerjemahan = LOAD_INDONESIA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listSurahAdapter = new ListSurahAdapter(new ArrayList<Surah>(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listSurahAdapter);

        mPresenter.loadSurah(loadTerjemahan);

    }

    @Override
    public ListSurahPresenter initPresenter() {
        return new ListSurahPresenter(this);
    }


    public void onLoad(ArrayList<Surah> data) {
        listSurahAdapter.refresh(data);
    }

    @Override
    public void onCLick(Surah surah) {
        Intent ayat = new Intent(QuranActivity.this, ListAyatActivity.class);
        ayat.putExtra(ListAyatActivity.KEY_AYAT, surah.getAyat());
        ayat.putExtra(ListAyatActivity.KEY_SURAH, surah.getSurah());
        ayat.putExtra(ListAyatActivity.KEY_TERJEMAHAN, surah.getTerjemahan());
        ayat.putExtra(ListAyatActivity.KEY_LOAD_TERJEMAHAN, loadTerjemahan);
        startActivity(ayat);
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
