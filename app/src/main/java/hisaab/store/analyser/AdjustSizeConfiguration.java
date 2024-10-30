package hisaab.store.analyser;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class AdjustSizeConfiguration {

    Activity context;

    int maxheight=0;
    public AdjustSizeConfiguration(Activity context){
        this.context=context;
    }
    public void adjustSize(View view) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        //params.height = (height*14)/100;         // 10%
        params.width = ((width * 83) / 100); // 20%
        view.setLayoutParams(params);

    }

    public void setfixWidth(View view, int percentage){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        params.width = ((width * percentage) / 100);
        view.setLayoutParams(params);
    }

    public void setfixHeight(View view, int percentage){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        view.setMinimumHeight((height * percentage) / 100);
        view.setLayoutParams(params);
    }
    public void setwidthTableColoumn(View view,int percentage){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        params.width = ((width * percentage) / 100)/3;
        view.setLayoutParams(params);
    }

    public void setHeightTableColoumn(View view,int height,int widthpercentage){
        if(height>maxheight) maxheight=height;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        view.setMinimumHeight(maxheight);
        params.width = ((width * widthpercentage) / 100)/3;
        view.setLayoutParams(params);
    }
}
