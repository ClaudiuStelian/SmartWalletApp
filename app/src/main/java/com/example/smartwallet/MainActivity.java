package com.example.smartwallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {




    //Initialize variable
    MeowBottomNavigation bottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Assign variable
        bottomNavigation=findViewById(R.id.bottom_navigation);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_wallet));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_payment));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_calendar));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_businesscard));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_settings));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;
                //Check condition
                switch (item.getId()){
                    case 1:
                        //When id is 1
                        //Initialize notification fragment
                        fragment = new WalletFragment();
                        break;
                    case 2:
                        //When id is 2
                        //Initialize payment fragment
                        fragment = new PaymentFragment();
                        break;
                    case 3:
                        //When id is 3
                        //Initialize calendar fragment
                        fragment= new CalendarFragment();
                        break;
                    case 4:
                        //WHen id si 4
                        //Initialize business fragment
                        fragment= new BusinesscardFragment();
                        break;
                    case 5:
                        //WHen id si 4
                        //Initialize business fragment
                        fragment= new SettingsFragment();
                        break;
                }
                //Load Fragment
                loadFragment(fragment);

            }


        });

        //Set notification count
//        bottomNavigation.setCount(1,"");
        //Set home fragment initially selected
        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Display toast

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Display toast

            }
        });
    }

    private void loadAppCompatActivity(AppCompatActivity appCompatActivity) {
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void emailto(View view){

        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse( "mailto:stelianclaudiu92@yahoo.ro" ) )  );

        finish();
    }

}