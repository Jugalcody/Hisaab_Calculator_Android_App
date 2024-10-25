package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hisaabcalculator.R;

public class Contact extends AppCompatActivity {

  AppCompatButton send;
  ImageView back;
  LinearLayout toolbar;
  EditText sub,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        sub=findViewById(R.id.contact_sub);
        back=findViewById(R.id.contact_back);
        msg=findViewById(R.id.contact_msg);
        send=findViewById(R.id.contact_send);
        toolbar=findViewById(R.id.contact_toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences sp=getSharedPreferences("theme",MODE_PRIVATE);
        if(sp.getString("theme","dark").equals("pink")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
            send.setBackgroundResource(R.drawable.loginbutpink);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));
            send.setBackgroundResource(R.drawable.loginbut);
        }
        ButtonEffect buttonEffect=new ButtonEffect(Contact.this);
        buttonEffect.buttonEffect(send);
        send.setOnClickListener(view->{
            if(!sub.getText().toString().equals("")) {
                if (!msg.getText().toString().equals("")) {
                    Intent i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jugalkd2000@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, sub.getText().toString());
                    i.setData(Uri.parse("mailto:"));
                    i.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());
                    try {
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(Contact.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Contact.this,"empty message!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(Contact.this,"empty topic!", Toast.LENGTH_LONG).show();
            }
        });
    }
}