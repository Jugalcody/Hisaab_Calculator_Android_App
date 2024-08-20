package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;

public class about extends AppCompatActivity {

  AppCompatButton send;
  ImageView back;
  EditText sub,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        sub=findViewById(R.id.contact_sub);
        back=findViewById(R.id.contact_back);
        msg=findViewById(R.id.contact_msg);
        send=findViewById(R.id.contact_send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }
        send.setOnClickListener(view->{
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{ "jugalkd2000@gmail.com" });
            i.putExtra(Intent.EXTRA_SUBJECT,sub.getText().toString());
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_TEXT,msg.getText().toString());
            try {
                startActivity(i);
            }
            catch (Exception e){
                Toast.makeText(about.this,e.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
}