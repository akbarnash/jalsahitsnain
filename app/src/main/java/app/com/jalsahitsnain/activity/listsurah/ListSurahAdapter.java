package app.com.jalsahitsnain.activity.listsurah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.models.modelquran.Surah;

/**
 * Created by User on 01/05/2018.
 */

public class ListSurahAdapter extends RecyclerView.Adapter<ListSurahAdapter.SurahHolder> {
    private ArrayList<Surah> surahList;
    private OnSurahItemClick click;

    ListSurahAdapter(ArrayList<Surah> surahList, OnSurahItemClick click) {
        this.surahList = surahList;
        this.click = click;
    }

    @Override
    public SurahHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_surah_layout, parent, false);
        return new SurahHolder(view);
    }

    @Override
    public void onBindViewHolder(SurahHolder holder, int position) {
        holder.setContent(surahList.get(position), click);
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    void refresh(ArrayList<Surah> fill){
        surahList = new ArrayList<>();
        surahList.addAll(fill);

        notifyDataSetChanged();
    }

    class SurahHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.rowSurah, R.id.rowAyat, R.id.rowTerjemahanSurah, R.id.rowJumlahAyat})
        List<TextView> rowSurah;

        SurahHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void setContent(final Surah surah, final OnSurahItemClick click) {
            rowSurah.get(0).setText(surah.getSurah());
            rowSurah.get(1).setText(surah.getAyat());
            rowSurah.get(2).setText(surah.getTerjemahan());
            rowSurah.get(3).setText(surah.getJumlahAyat());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onCLick(surah);
                }
            });

        }

    }

    interface OnSurahItemClick {
        void onCLick(Surah surah);
    }
}
