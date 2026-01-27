package hisaab.store.analyser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ProfileFragment extends Fragment {

    ImageView setting,nameedit;
    ShapeableImageView userimg;

    LinearLayout profilelayout;

    FloatingActionButton addphoto;
    RelativeLayout upperlayout;
    ScrollView parentLayout;
    LinearLayout logoutlayout;
    Utils utils;
    TextView name1,name2,dob,phone,nametitle,dobtitle,phonetitle;

    int REQUEST_IMAGE_PICKER = 1;

    ViewGroup root;
    SharedPreferences spitem,splogin,sptheme;
    public static UserViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        setting=view.findViewById(R.id.imgSettings_profile);
        logoutlayout=view.findViewById(R.id.logoutlayout);
        utils=new Utils();
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        parentLayout=view.findViewById(R.id.profileFragment_parentlayout);
        name1=view.findViewById(R.id.txtProfileName);
        name2=view.findViewById(R.id.textprofilename2);
        profilelayout=view.findViewById(R.id.profile_cardlayout);
        dob=view.findViewById(R.id.profiledob);
        nametitle=view.findViewById(R.id.profilename_title);
        phonetitle=view.findViewById(R.id.profilephone_title);
        upperlayout=view.findViewById(R.id.profilecard_upperlayout);
        dobtitle=view.findViewById(R.id.profiledob_title);
        phone=view.findViewById(R.id.profilephone);
        nameedit=view.findViewById(R.id.profile_nameedit);
        userimg=view.findViewById(R.id.imgProfile);
        root = (ViewGroup) requireActivity().getWindow().getDecorView().getRootView();
        addphoto=view.findViewById(R.id.fabChangeProfilePic);

        sptheme = requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE);
        splogin = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        spitem = requireActivity().getSharedPreferences("item", Context.MODE_PRIVATE);

        dob.setText(spitem.getString("dob", "Null"));
        phone.setText(spitem.getString("user", "Null"));

        changetheme();
        try {

            viewModel.getUserName().observe(getViewLifecycleOwner(), newName -> {
                name1.setText(newName);
                name2.setText(newName);
            });

            nameedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utils.createWindowname(requireActivity(),root,name1);
                    name1.setText(splogin.getString("fullname", "Null"));
                    name2.setText(splogin.getString("fullname", "Null"));
                }
            });

            name1.setText(splogin.getString("fullname", "Null"));
            name2.setText(splogin.getString("fullname", "Null"));
            if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                userimg.setImageBitmap(Utils.decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
            } else {
                userimg.setImageResource(R.drawable.baseline_person_24);
            }
        } catch (Exception e) {
            userimg.setImageResource(R.drawable.baseline_person_24);
        }
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        logoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogoutDialog();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SettingActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }



    public void updateUserLoginStatus(String phone, Boolean status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy & HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        Map<String, Object> updates = new HashMap<>();
        updates.put("isLoggedIn", status);
        updates.put(status ? "lastLoggedInDate" : "lastLoggedOutDate", currentDateTime);

        db.collection("HisaabAnalyser")
                .document("users")
                .collection("userList")
                .document(phone)
                .update(updates)
                .addOnFailureListener(e -> System.out.println(e.getMessage()));
    }

    private void openLogoutDialog() {
        AlertDialog.Builder a = new AlertDialog.Builder(requireContext());
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("Yes", (dialog, i) -> {
            try {

                splogin.edit().putBoolean("islogged", false).apply();
                splogin.edit().putBoolean("isLoginstatusSent", false).apply();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                splogin.edit().putString("logoutdate", sdf.format(new Date())).apply();

                updateUserLoginStatus(spitem.getString("user", ""), false);

                startActivity(new Intent(requireActivity(), MainActivity.class));
                requireActivity().finish();
            }
            catch (Exception e){
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });

        a.setNegativeButton("No", (dialog, i) -> dialog.dismiss());

        AlertDialog alert = a.create();
        alert.show();

        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            alert.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                if (imageUri != null) {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().getContentResolver(),
                            imageUri
                    );


                    String key = spitem.getString("user", "") + "img";
                    splogin.edit().putString(key, encodeBitmapToString(bitmap)).apply();

                    viewModel.setProfileImage(encodeBitmapToString(bitmap));
                    userimg.setImageBitmap(bitmap);
                }

                utils.clearDim(root);


            } catch (Exception e) {
                e.printStackTrace();
                utils.clearDim(root);

                Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else {
            utils.clearDim(root);

        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        utils.applyDim(root, 0.5f);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICKER);
    }



    public static String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeStringToBitmap(String encodedBitmap) {
        byte[] decoded = Base64.decode(encodedBitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
    }


    @Override
    public void onResume() {
        super.onResume();
        changetheme();
        try {

            name1.setText(splogin.getString("fullname", "Null"));
            name2.setText(splogin.getString("fullname", "Null"));

            if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                userimg.setImageBitmap(Utils.decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
            } else {
                userimg.setImageResource(R.drawable.baseline_person_24);
            }
        } catch (Exception e) {
            userimg.setImageResource(R.drawable.baseline_person_24);
        }
    }




    private void changetheme() {
        profilelayout.setBackground(getResources().getDrawable(R.drawable.cardlayoutbg));

        if (sptheme.getString("theme", "dark").equals("pink")) {
               parentLayout.setBackgroundColor(getResources().getColor(R.color.white));

            profilelayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(),R.color.backgroundlight2));
            name2.setTextColor(getResources().getColor(R.color.black));
            phone.setTextColor(getResources().getColor(R.color.black));
            dob.setTextColor(getResources().getColor(R.color.black));

            nameedit.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.black));
            phonetitle.setTextColor(getResources().getColor(R.color.black));
            dobtitle.setTextColor(getResources().getColor(R.color.black));
            nametitle.setTextColor(getResources().getColor(R.color.black));
            upperlayout.setBackground(getResources().getDrawable(R.drawable.profilecardbackground));


        }
        else{
            parentLayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));


            profilelayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), androidx.cardview.R.color.cardview_dark_background)
            );



            name2.setTextColor(getResources().getColor(R.color.white));
            phone.setTextColor(getResources().getColor(R.color.white));
            dob.setTextColor(getResources().getColor(R.color.white));
            nameedit.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));

            nametitle.setTextColor(getResources().getColor(R.color.white));
            phonetitle.setTextColor(getResources().getColor(R.color.white));
            dobtitle.setTextColor(getResources().getColor(R.color.white));
            upperlayout.setBackground(getResources().getDrawable(R.drawable.profilecardbackground_dark));


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            profilelayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            profilelayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }

    }

        }