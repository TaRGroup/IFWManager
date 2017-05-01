package trumeet.IfwManager.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import trumeet.IfwManager.R;

/**
 * Created by Trumeet on 2017/3/27.
 */

public abstract class SimpleFragmentActivity extends Activity {
    private ProgressBar mProgressBar;
    private ViewGroup mProgressLayout;
    private TextView mProgressText;
    private TextView mText;
    public interface OnBackPressedListener {
        boolean onBackPressed ();
    }
    private OnBackPressedListener onBackPressedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressLayout = (ViewGroup) findViewById(R.id.progress_layout);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        mText = (TextView) findViewById(R.id.text1);
    }

    public void showProgress (boolean show, @Nullable String text) {
        if (TextUtils.isEmpty(text)) {
            mProgressText.setVisibility(View.GONE);
        } else {
            mProgressText.setVisibility(View.VISIBLE);
            mProgressText.setText(text);
        }
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    public void setText (@Nullable CharSequence text) {
        if (text == null || TextUtils.isEmpty(text)) {
            mText.setVisibility(View.GONE);
            return;
        }
        mText.setVisibility(View.VISIBLE);
        mText.setText(text);
    }

    public void setText (CharSequence text, @StyleRes int resId) {
        setText(text);
        mText.setTextAppearance(this, resId);
    }

    public void setTextColor (int color) {
        mText.setTextColor(color);
    }

    public void showBackIcon (boolean b) {
        getActionBar().setDisplayHomeAsUpEnabled(b);
    }

    public void showBackIcon (boolean show, @DrawableRes int id) {
        showBackIcon(show);
        setBackIcon(id);
    }

    public void showBackIcon (boolean show, Drawable drawable) {
        showBackIcon(show);
        setBackIcon(drawable);
    }

    public void setBackIcon (@DrawableRes int id) {
        getActionBar().setHomeAsUpIndicator(id);
    }

    public void setBackIcon (Drawable drawable) {
        getActionBar().setHomeAsUpIndicator(drawable);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void turnFragment (Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.frame, fragment)
                .commitAllowingStateLoss();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed () {
        if (onBackPressedListener != null) {
            if (!onBackPressedListener.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();

    }
}
