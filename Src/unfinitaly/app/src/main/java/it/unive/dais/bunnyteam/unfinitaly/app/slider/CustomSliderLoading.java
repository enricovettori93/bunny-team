package it.unive.dais.bunnyteam.unfinitaly.app.slider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import it.unive.dais.bunnyteam.unfinitaly.app.LoadingActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.view.CustomIntroFragment;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */

@Deprecated
public class CustomSliderLoading extends CustomIntroFragment {
    private TextView tv_status;
    private TextView tvCountLoad;
    private ProgressBar progressBar;
    private LoadingActivity loadAct;
    private AVLoadingIndicatorView loadinggif;
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private View v;

    public static CustomSliderLoading newInstance(int layoutResId, LoadingActivity loadAct) {
        CustomSliderLoading sampleSlide = new CustomSliderLoading();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(args);
        sampleSlide.loadAct = loadAct;
        return sampleSlide;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(v!=null)
            return v;
        else
            return inflater.inflate(layoutResId, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = view;
            super.onViewCreated(view, savedInstanceState);
            //tv_status = (TextView) view.findViewById(R.id.tv_status2);
            tvCountLoad = (TextView) view.findViewById(R.id.tvCountLoad2);
            //progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
            loadinggif = (AVLoadingIndicatorView) view.findViewById(R.id.avi2);
        }
    }
}
