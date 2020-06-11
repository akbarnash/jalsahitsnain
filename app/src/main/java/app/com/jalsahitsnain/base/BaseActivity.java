package app.com.jalsahitsnain.base;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import app.com.jalsahitsnain.database.DatabaseContract;


public abstract class BaseActivity<PT extends BasePresenter> extends AppCompatActivity {

    public PT mPresenter;
    public String LOAD_INDONESIA = DatabaseContract.LOAD_TERJEMEMAHAN_INDONESIA;

    public abstract PT initPresenter();

    private Unbinder unbinder;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        unbinder = ButterKnife.bind(this);
        mPresenter = initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    public void startActivity(Class target) {
        startActivity(new Intent(this, target));
    }
}
