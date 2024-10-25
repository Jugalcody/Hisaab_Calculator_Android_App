package hisaab.store.analyser;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.hisaabcalculator.R;

public class WarnMessage {

    public void createWindow(Activity context) {


        ViewGroup root=(ViewGroup) context.getWindow().getDecorView().getRootView();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.warn_window, null);

        ImageView pback=popup.findViewById(R.id.popup_back);
        CheckBox checkBox=popup.findViewById(R.id.popup_warn_check);
        AppCompatButton gotit=popup.findViewById(R.id.popup_warn_gotit);
        SharedPreferences sp2 = context.getSharedPreferences("theme",context.MODE_PRIVATE);
        if (sp2.getString("theme", "dark").equals("pink")) {
            gotit.setBackgroundColor(context.getColor(R.color.primarypink));
        }
        else{
            gotit.setBackgroundColor(context.getColor(R.color.primarydark));
        }
        TextView title=popup.findViewById(R.id.warn_popup_title);
        SharedPreferences spitem = context.getSharedPreferences("item", context.MODE_PRIVATE);
        SharedPreferences sp= context.getSharedPreferences("login", context.MODE_PRIVATE);
        if(sp.getBoolean("showwarn",true)) {
            checkBox.setChecked(false);
        }
        else{
            checkBox.setChecked(true);
        }
        title.setText("Hii "+spitem.getString("name","").trim().substring(0,1).toUpperCase()+spitem.getString("name","").trim().substring(1).toLowerCase()+" , hope you are using the app well");
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 55;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // Create and show the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popup,
                width,
                height,
                true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getY() < height || motionEvent.getY() >view.getBottom()) return true;
                return false;
            }
        });
        popupWindow.update();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean("showwarn",false).apply();
                }
                else{
                    sp.edit().putBoolean("showwarn",true).apply();
                }
            }
        });

        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if(context instanceof display){
            LinearLayout layout=context.findViewById(R.id.first_parent_layout);
            layout.post(new Runnable() {
                @Override
                public void run() {

                    applyDim(root,0.5f);
                    popupWindow.showAtLocation(layout, Gravity.CENTER,0,0);


                }
            });

        }
        else if(context instanceof first){
            CoordinatorLayout layout=context.findViewById(R.id.first_parent_layout);
            layout.post(new Runnable() {
                @Override
                public void run() {

                    applyDim(root,0.5f);
                    popupWindow.showAtLocation(layout, Gravity.CENTER,0,0);


                }
            });

        }

    }


    public  void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
}
