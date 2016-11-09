package edu.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		/*
		 * до создания активности сделать
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);//скрытие заголовка окна
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected Fragment createFragment() 
	{
		
		return new CrimeCameaFragment();
	}

}
