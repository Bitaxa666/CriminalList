package edu.example.criminalintent;

import java.util.UUID;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class CrimeActivity extends SingleFragmentActivity
{
	//Context cont;//ссылка на активити
    @Override
   protected Fragment createFragment()
    {
    	//cont=this;
    	//return new CrimeFragment();
    	
    	UUID crimeId=(UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
    	return CrimeFragment.newInstance(crimeId);
    }

    

}
