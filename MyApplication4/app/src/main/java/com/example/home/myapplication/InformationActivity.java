package com.example.home.myapplication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends AppCompatActivity {
    static TextView tvInfoName,tvInfoAdd,tvInfoPhone,tvInfoWebsite,tvInfoLike, tvLikeLable;
    static ImageView ivInfoPic,ivInfoLikePic;
    static String photoLink="";
    static String strdata;
    static String comeFrom ="";
    static Button bInfoBookmark;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        retrieveData.callfrom = strdata;
        this.finish();
        //NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        final Intent intent = getIntent();

        int position = intent.getIntExtra("position", 0);
        int pos = BookmarkFragment.getPosition();


        if(intent !=null) {
            strdata = intent.getExtras().getString("unique_page");

        }


        ivInfoLikePic = (ImageView) findViewById(R.id.ivInfoLikePic);
        ivInfoPic = (ImageView) findViewById(R.id.ivInfoPic);
        tvInfoName = (TextView) findViewById(R.id.tvInfoName);
        tvInfoAdd = (TextView) findViewById(R.id.tvInfoAdd);
        tvInfoPhone = (TextView) findViewById(R.id.tvInfoPhone);
        tvInfoWebsite = (TextView) findViewById(R.id.tvInfoWebsite);
        tvInfoLike = (TextView) findViewById(R.id.tvInfoLike);
        tvLikeLable = (TextView)findViewById(R.id.tvLikeLabel);
        bInfoBookmark = (Button)findViewById(R.id.bInfoBookmark);

        ivInfoLikePic.setImageResource(R.drawable.unlike);
        tvInfoName.setText(retrieveData.title);
        tvInfoLike.setText(retrievePostLike.total_like);
        tvInfoAdd.setText(retrieveData.address);
        tvInfoPhone.setText(retrieveData.phoneNo);
        tvInfoWebsite.setText(retrieveData.website);




       if(retrieveData.callfrom.equals("InformationActivity")){
           if(retrievePostLike.code.equals("true")){
               ivInfoLikePic.setImageResource(R.drawable.like);
           }else{
               ivInfoLikePic.setImageResource(R.drawable.unlike);
           }

       }


        if (Bookmark.findID(retrieveData.place_id) == null) {
            bInfoBookmark.setText("Bookmark");

        } else {
            bInfoBookmark.setText("Unbookmark");
        }




        if (retrieveData.resImage!=null){
            ivInfoPic.setImageBitmap(retrieveData.resImage);
        }else{
            ivInfoPic.setImageResource(R.drawable.no_image_thumb);
        }


        //allow update like to be call on the total number of like
        tvInfoLike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLike();
            }
        });

        ///allow update like to be call on the like label
        tvLikeLable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLike();
            }
        });

        //allow update like to be call on the like picture
        ivInfoLikePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLike();
            }
        });



        bInfoBookmark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if(strdata.equals("HomeFragment")){
                    if (Bookmark.findID(retrieveData.place_id) == null) {
                        Bookmark.bookmarkArrayList.add(new Bookmark(retrieveData.place_id, retrieveData.title));
                        bookmarkAsyncTask task = new bookmarkAsyncTask();
                        task.execute("bookmark", retrieveData.place_id, retrieveData.title);
                        Toast.makeText(InformationActivity.this, "Bookmarked!", Toast.LENGTH_SHORT).show();
                        bInfoBookmark.setText("Unbookmark");
                        HomeFragment.bBookmark.setText("Unbookmark");

                    } else {
                        Bookmark.deleteID(retrieveData.place_id);
                        Toast.makeText(InformationActivity.this, "Bookmark deleted", Toast.LENGTH_SHORT).show();
                        bInfoBookmark.setText("Bookmark");
                        HomeFragment.bBookmark.setText("Bookmark");
                    }

                }

                if(strdata.equals("BookmarkFragment")){
                    Bookmark.deleteID(retrieveData.place_id);
                    Toast.makeText(InformationActivity.this, "Bookmark deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();





                   // NavUtils.navigateUpFromSameTask(InformationActivity.this);
                   // this.finish();


                }

/**
                if(retrieveData.callfrom.equals("InformationActivity")){
                    BookmarkFragment fragment = new BookmarkFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();

                }else{
                    //HomeFragment.bBookmark.setText("Bookmark");
                }
**/

            }
        });

    }


    public static void tvInfoLike(String total_like) {
        tvInfoLike.setText(total_like);
    }

    public void updateLike(){
        retrievePostLike taskPostLike = new retrievePostLike();
        taskPostLike.execute("setPostLike", retrieveData.place_id);

    }

}