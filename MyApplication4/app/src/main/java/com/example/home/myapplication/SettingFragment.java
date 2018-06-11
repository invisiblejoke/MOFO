package com.example.home.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class SettingFragment extends Fragment {

    static String radius = Integer.toString(2000);
    static String keyword ="";


    public static String getRadius() {
        return radius;
    }

    public static String getKeyword() {
        return keyword;
    }


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_setting, container, false);
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);


        //initialize seekbar,
        //set the starting point
        //set the incremental difference
        //set the max value


        final SeekBar distance = (SeekBar) rootView.findViewById(R.id.sbdistance);

        distance.setProgress(User.getDistance());
        distance.incrementProgressBy(500);
        distance.setMax(5000);




        final TextView distancetv = (TextView)rootView.findViewById(R.id.distancetv);

        final Switch cafe = (Switch) rootView.findViewById(R.id.cafeswitch);
        final Switch western = (Switch) rootView.findViewById(R.id.westernswitch);
        final Switch chinese = (Switch) rootView.findViewById(R.id.chineseswitch);

        final Switch bar = (Switch) rootView.findViewById(R.id.barswitch);

        final Switch japanese = (Switch) rootView.findViewById(R.id.japaneseswitch);
        final Switch korean = (Switch) rootView.findViewById(R.id.koreanswitch);
        final Switch mamak = (Switch) rootView.findViewById(R.id.mamakswitch);
        final Switch halal = (Switch) rootView.findViewById(R.id.halalswitch);
        final Switch bakery = (Switch) rootView.findViewById(R.id.bakeryswitch);


        // restore user last choice setting

        String userLastSetting = User.userSetting;
        String userLastSettings = userLastSetting.replace('|', ' ');
        //  for(int i =0; i<userLastSettings.length();i++) {
        if (userLastSettings.indexOf("cafe") >= 0) {
            cafe.setChecked(true);
        }
        if (userLastSettings.indexOf("western") >= 0) {
            western.setChecked(true);
        }
        if (userLastSettings.indexOf("chinese") >= 0) {
            chinese.setChecked(true);
        }
        if (userLastSettings.indexOf("bar" ) >= 0) {
            bar.setChecked(true);
        }
        if (userLastSettings.indexOf("japanese") >= 0) {
            japanese.setChecked(true);
        }
        if (userLastSettings.indexOf("korean") >= 0) {
            korean.setChecked(true);
        }
        if (userLastSettings.indexOf("mamak") >= 0) {
            mamak.setChecked(true);
        }
        if (userLastSettings.indexOf("bakery") >= 0) {
            bakery.setChecked(true);
        }
        if (userLastSettings.indexOf("halal") >= 0) {
            halal.setChecked(true);
        }
        User.initiateSetting =true;


        new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("info","onChecked");
                User.userSetting=keyword;

            }

        };







        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress = progress / 1000;
                    progress = progress * 1000;
                    float km = progress / 1000;
                    distancetv.setText("Distance (" + km + "KM)");
                    radius = Integer.toString(progress);
                if ( progress<999) {
                    distancetv.setText("Distance (0.5KM)");
                    radius = Integer.toString(500);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        cafe.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cafe.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword += "|cafe";
                    User.setUserSetting(keyword);
                    //cafe.setChecked(true);
                } else {
                    // The toggle is disabled
                    cafe.setTextOff("off");

                    //find the char, replace with empty string
                   // String s = "|cafe";
                  // int start = keyword.indexOf(s);
                  //  int end = (keyword.indexOf(s)+4);
                   // StringBuilder build= new StringBuilder(keyword);
                  //  build.deleteCharAt(end-start);
                  //  keyword = build.toString();
                 //  String remove = "\\s*\\b|cafe\\b\\s*";
                    String a= keyword.replace("|cafe", "");
                    keyword = a;
                    User.setUserSetting(keyword);
                    //cafe.setChecked(false);

                }
            }
        });

        western.setOnCheckedChangeListener(new  Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    western.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword+= "|western";
                    User.setUserSetting(keyword);

                    // western.setChecked(true);
                } else {
                    // The toggle is disabled
                    western.setTextOff("off");

                    //find the word and remove it
                    //String remove = "\\s*\\b|western\\b\\s*";
                    //keyword = keyword.replaceAll(remove,"");
                    String a = keyword.replace("|western","");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // western.setChecked(false);


                }
            }
        });
        chinese.setOnCheckedChangeListener(new  Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    chinese.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword += "|chinese";
                    User.setUserSetting(keyword);

                    // chinese.setChecked(true);
                } else {
                    // The toggle is disabled
                    chinese.setTextOff("off");

                    //find the word and remove it
                    //String remove = "\\s*\\b|chinese\\b\\s*";
                   //keyword = keyword.replaceAll(remove,"");
                    String a = keyword.replace("|chinese","");
                    keyword = a;
                    User.setUserSetting(keyword);
                    //chinese.setChecked(false);
                }
            }
        });
        bar.setOnCheckedChangeListener(new  Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    bar.setTextOn("on");

                    //add into the list and indicate variable to run
                   keyword+="|bar";
                    User.setUserSetting(keyword);

                } else {
                    // The toggle is disabled
                    bar.setTextOff("off");

                    //loop and find the variable to delete
                    //String remove = "\\s*\\b|bar\\b\\s*";
                    //keyword = keyword.replaceAll(remove,"");
                    String a= keyword.replace("|bar","");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // indian.setChecked(false);
                }
            }
        });
        japanese.setOnCheckedChangeListener(new  Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    japanese.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword+= "|japanese";
                    User.setUserSetting(keyword);
                    // japanese.setChecked(true);
                } else {
                    // The toggle is disabled
                    japanese.setTextOff("off");

                    //find the word and remove it
                    //String remove = "\\s*\\b|japanese\\b\\s*";
                    //keyword = keyword.replaceAll(remove,"");
                    String  a = keyword.replace("|japanese","");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // japanese.setChecked(false);
                }
            }
        });
        korean.setOnCheckedChangeListener(new  Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    korean.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword+="|korean";
                    User.setUserSetting(keyword);
                    // korean.setChecked(true);
                } else {
                    // The toggle is disabled
                    korean.setTextOff("off");

                    //find the word and remove it
                    //String remove = "\\s*\\b|korean\\b\\s*";
                    //keyword = keyword.replaceAll(remove,"");
                    String a = keyword.replace("|korean","");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // korean.setChecked(false);
                }
            }
        });
        mamak.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    mamak.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword += "|mamak";
                    User.setUserSetting(keyword);
                    //mamak.setChecked(true);
                } else {
                    // The toggle is disabled
                    mamak.setTextOff("off");

                    //find the word and remove it
                    //String remove = "\\s*\\b|mamak\\b\\s*";
                    //keyword = keyword.replaceAll(remove, "");
                    String a= keyword.replace("|mamak", "");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // mamak.setChecked(false);
                }
            }
        });

        halal.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    halal.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword += "|halal";
                    User.setUserSetting(keyword);
                    // korean.setChecked(true);
                } else {
                    // The toggle is disabled
                    halal.setTextOff("off");

                    //find the word and remove it
                   // String remove = "\\s*\\b|halal\\b\\s*";
                    //keyword = keyword.replaceAll(remove, "");
                    String a = keyword.replace("|halal", "");
                    keyword = a;
                    User.setUserSetting(keyword);
                    // korean.setChecked(false);
                }
            }
        });



        bakery.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    bakery.setTextOn("on");

                    //add into the list and indicate variable to run
                    keyword += "|bakery";
                    User.setUserSetting(keyword);
                    Log.i("keyword", User.getUserSetting());
                    // vegetarian.setChecked(true);
                } else {
                    // The toggle is disabled
                    bakery.setTextOff("off");

                    //loop and find the variable to delete
                   // String remove = "\\s*\\b|bakery\\b\\s*";
                   // keyword = keyword.replaceAll(remove, "");
                    keyword = keyword.replace("|bakery", "");
                    User.setUserSetting(keyword);
                    // vegetarian.setChecked(false);
                }
            }
        });

        return rootView;
    }
}
