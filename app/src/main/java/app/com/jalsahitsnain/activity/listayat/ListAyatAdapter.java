package app.com.jalsahitsnain.activity.listayat;

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
import app.com.jalsahitsnain.models.modelquran.Ayat;

/**
 * Created by User on 01/05/2018.
 */

public class ListAyatAdapter extends RecyclerView.Adapter<ListAyatAdapter.AyatHolder> {

    private ArrayList<Ayat> ayatList;

    ListAyatAdapter(ArrayList<Ayat> data) {
        this.ayatList = data;
    }

    @Override
    public AyatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ayat_layout, parent, false);
        return new AyatHolder(view);
    }

    @Override
    public void onBindViewHolder(AyatHolder holder, int position) {
        holder.setContent(ayatList.get(position));
    }

    @Override
    public int getItemCount() {
        return ayatList.size();
    }

    void refresh(ArrayList<Ayat> fill) {
        ayatList = new ArrayList<>();
        ayatList.addAll(fill);

        notifyDataSetChanged();
    }

    class AyatHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.rowAyat, R.id.rowArabic, R.id.rowTerjemahan})
        List<TextView> rowAyat;

        AyatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setContent(Ayat ayat) {
            rowAyat.get(0).setText(ayat.getAyat());
            rowAyat.get(1).setText(ayat.getArab());
            rowAyat.get(2).setText(ayat.getTerjemahan());
        }
    }
}
