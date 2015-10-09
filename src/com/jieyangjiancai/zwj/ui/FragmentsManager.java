package com.jieyangjiancai.zwj.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jieyangjiancai.zwj.R;

public class FragmentsManager {
	public static FragmentManager mFragMgr;  
	
	public static void Init(FragmentActivity activity)
	{
		mFragMgr = activity.getSupportFragmentManager();
	}
	
	public static void InitFragment(Fragment f){  
        ChangeFragment(f, true);  
    }
	public static void ChangeFragment(Fragment f){  
        ChangeFragment(f, true);  
    }
	public static void AddFragment(Fragment f){  
        ChangeFragment(f, false);  
    }
	
    private static void ChangeFragment(Fragment f, boolean init){  
        FragmentTransaction ft = mFragMgr.beginTransaction();  
        ft.replace(R.id.layout_fragment, f);  
        if(!init)  
            ft.addToBackStack(null);  
        ft.commit();
    }  
    public static void BackFragment(){  
    	mFragMgr.popBackStack();
    } 
    
//	public static void ShowFragment(Fragment f)
//	{
//		FragmentTransaction ft = mFragMgr.beginTransaction();  
//		ft.show(f);
//        ft.commit();
//	}
}
