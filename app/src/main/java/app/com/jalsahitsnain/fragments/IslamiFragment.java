package app.com.jalsahitsnain.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import app.com.jalsahitsnain.R;
import app.com.jalsahitsnain.activity.ArahKiblatActivity;
import app.com.jalsahitsnain.activity.listsurah.QuranActivity;
import app.com.jalsahitsnain.util.Server;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IslamiFragment extends Fragment{

    @BindView(R.id.tv_tanngal)
    TextView tvTanngal;
    @BindView(R.id.tv_nama_daerah)
    TextView tvNamaDaerah;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.txtSubuh)
    TextView txtSubuh;
    @BindView(R.id.img_subuh)
    ImageView imgSubuh;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.txtDzuhur)
    TextView txtDzuhur;
    @BindView(R.id.img_zuhur)
    ImageView imgZuhur;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.txtAshar)
    TextView txtAshar;
    @BindView(R.id.img_ashar)
    ImageView imgAshar;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.txtMaghrib)
    TextView txtMaghrib;
    @BindView(R.id.img_magrhib)
    ImageView imgMagrhib;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.txtIsya)
    TextView txtIsya;
    @BindView(R.id.img_isya)
    ImageView imgIsya;
    @BindView(R.id.img_arah_kabah)
    ImageView imgArahKabah;
    @BindView(R.id.img_al_quran)
    ImageView imgAlQuran;
    @BindView(R.id.swipe_id)
    SwipeRefreshLayout swipeId;
    CoordinatorLayout idHomeActivity;
    ImageView imgbg;
    ProgressBar pgb;
    CardView cardViewKiblat, cardViewQuran;

    public IslamiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_islami, container, false);
        ButterKnife.bind(getActivity());

        idHomeActivity = view.findViewById(R.id.id_home_activity);
        swipeId = view.findViewById(R.id.swipe_id);
        txtAshar = view.findViewById(R.id.txtAshar);
        txtDzuhur = view.findViewById(R.id.txtDzuhur);
        txtIsya = view.findViewById(R.id.txtIsya);
        txtMaghrib = view.findViewById(R.id.txtMaghrib);
        txtSubuh = view.findViewById(R.id.txtSubuh);
        tvNamaDaerah = view.findViewById(R.id.tv_nama_daerah);
        tvTanngal = view.findViewById(R.id.tv_tanngal);
        imgAlQuran = view.findViewById(R.id.img_al_quran);
        imgArahKabah = view.findViewById(R.id.img_arah_kabah);
        imgbg = view.findViewById(R.id.img_bg);
        pgb = view.findViewById(R.id.progress_bar);
        imgSubuh = view.findViewById(R.id.img_subuh);
        imgZuhur = view.findViewById(R.id.img_zuhur);
        imgAshar = view.findViewById(R.id.img_ashar);
        imgMagrhib = view.findViewById(R.id.img_magrhib);
        imgIsya = view.findViewById(R.id.img_isya);
        cardViewKiblat = view.findViewById(R.id.cvKiblat);
        cardViewQuran = view.findViewById(R.id.cvQuran);

        jsonParse();
        imageGreet();
        refresh();
        klikimg();

        return view;
    }

    private void jsonParse() {
        AndroidNetworking.get(Server.API_ALADHAN)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(@NotNull JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONObject timings = data.getJSONObject("timings");
                            String fajr = timings.getString("Fajr");
                            String dhuhr = timings.getString("Dhuhr");
                            String asr = timings.getString("Asr");
                            String maghrib = timings.getString("Maghrib");
                            String isha = timings.getString("Isha");
                            JSONObject date = data.getJSONObject("date");
                            String readable = date.getString("readable");
                            JSONObject hijri = date.getJSONObject("hijri");
                            String day = hijri.getString("day");
                            String year = hijri.getString("year");
                            JSONObject month = hijri.getJSONObject("month");
                            String en = month.getString("en");

                            txtSubuh.setText(fajr+" WIB");
                            txtDzuhur.setText(dhuhr+" WIB");
                            txtAshar.setText(asr+" WIB");
                            txtMaghrib.setText(maghrib+" WIB");
                            txtIsya.setText(isha+" WIB");

                            tvTanngal.setText(readable+"\n"+day+" "+en+" "+year+" H");
                            tvNamaDaerah.setText("Kuningan, Jawa Barat\nIndonesia");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pgb.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(ANError anError) {
                        //Snackbar.make(idHomeActivity, "Gagal Mendapatkan Data", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_LONG).show();
                        pgb.setVisibility(View.GONE);
                    }
                });
    }
    private void imageGreet(){
        Calendar calendar = Calendar.getInstance();
        int timeofday = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeofday >= 0 && timeofday < 6){
            imgbg.setImageResource(R.drawable.bg_header_dawn);
        }else if (timeofday >= 6 && timeofday < 12) {
            imgbg.setImageResource(R.drawable.bg_header_sunrise);
        } else if (timeofday >= 12 && timeofday < 16) {
            imgbg.setImageResource(R.drawable.bg_header_daylight);
        } else if (timeofday >= 16 && timeofday < 18) {
            imgbg.setImageResource(R.drawable.bg_header_evening);
        } else if (timeofday >= 18 && timeofday < 24) {
            imgbg.setImageResource(R.drawable.bg_header_night);
        }
    }
    private void refresh() {
        swipeId.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary);
        swipeId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swipeId.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        jsonParse();
                    }
                }, 2000);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void klikimg(){
        cardViewQuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuranActivity.class));
            }
        });
        cardViewKiblat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ArahKiblatActivity.class));
            }
        });
        imgSubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });
        imgZuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });
        imgAshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });
        imgMagrhib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });
        imgIsya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
