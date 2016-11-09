package edu.example.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks
{

	@Override
	protected Fragment createFragment() {
		
		return new CrimeListFragment();
	}

	@Override
	protected int getLayoutResId() 
	{		
		//return R.layout.activity_twopane;
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCrimeSelected(Crime crime)
	{
		//определяем если запущен макет для Таблетки, то открываем макет для него с дополнениемэ
		if(findViewById(R.id.detailFragmentConteiner)==null)
		{
			//Запускаем экземпляр для крайм пейджер активити
			Intent i=new Intent(this,CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(i);
		} else
		{
			FragmentManager fm=getSupportFragmentManager();
			android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
			Fragment oldDetail=fm.findFragmentById(R.id.detailFragmentConteiner);
			Fragment newDetail=CrimeFragment.newInstance(crime.getId());
			if(oldDetail!=null)
			{
				ft.remove(oldDetail);
			}
			ft.add(R.id.detailFragmentConteiner, newDetail);
			ft.commit();
		}
				
	}
	public void onCrimeUpdated(Crime crime)
	{
		FragmentManager fm=getSupportFragmentManager();
		CrimeListFragment listFragment=(CrimeListFragment) fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}
	

}
