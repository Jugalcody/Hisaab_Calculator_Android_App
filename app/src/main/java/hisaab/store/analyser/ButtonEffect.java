package hisaab.store.analyser;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;

import com.example.hisaabcalculator.R;

public class ButtonEffect {

    private Context context;

    // Constructor to initialize context
    public ButtonEffect(Context context) {
        this.context = context;
    }

    public void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // Using color from colors.xml
                        int colorFromResources = ContextCompat.getColor(context, R.color.transparent); // Replace with your color resource
                        // Or using a hex color code
                        int colorFromHex = Color.parseColor("#FF5722"); // Example hex code

                        v.getBackground().setColorFilter(colorFromResources, PorterDuff.Mode.SRC_OVER);
                        // Alternatively, you can use the hex color: v.getBackground().setColorFilter(colorFromHex, PorterDuff.Mode.SRC_OVER);

                        v.invalidate();
                        v.animate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                    default:
                        v.getBackground().clearColorFilter();
                }
                return false;
            }
        });
    }
}
