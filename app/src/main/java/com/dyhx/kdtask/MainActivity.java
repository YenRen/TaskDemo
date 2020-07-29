package com.dyhx.kdtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    TaskFragment taskFragment;
    public static int DAY = -1;
    public static int HOUR = -1;
    boolean isHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskFragment = new TaskFragment();
        replaceFragment(taskFragment);
    }


    public void  replaceFragment(Fragment fragment){
        isHidden = false;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//注意！每次要执行commit()方法的时候都需要重新获取一次FragmentTransaction，否则用已经commit过的FragmentTransaction再次commit会报错！
        transaction.replace(R.id.fragmentTest,fragment);
        transaction.commit();
    }

    public void viewHidden(View view) {

        if(isHidden){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();//注意！每次要执行commit()方法的时候都需要重新获取一次FragmentTransaction，否则用已经commit过的FragmentTransaction再次commit会报错！
            transaction.show(taskFragment);
            transaction.commit();
            isHidden = false;
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();//注意！每次要执行commit()方法的时候都需要重新获取一次FragmentTransaction，否则用已经commit过的FragmentTransaction再次commit会报错！
            transaction.hide(taskFragment);
            transaction.commit();
            isHidden = true;
        }

    }
}
