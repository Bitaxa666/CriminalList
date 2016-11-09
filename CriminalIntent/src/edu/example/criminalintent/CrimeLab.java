package edu.example.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab 
{
	private static final String TAG="CrimeLab";
	private static final String FILENAME="crimes.json";
	private CriminalIntentJSONSerializer mSerializer;
	
	private ArrayList<Crime> mCrimes;
	private static CrimeLab sCrimeLab;//зачем ссылается на сам себя?
	private Context mAppContext;
	
	private CrimeLab(Context appContext)
	{
		mAppContext=appContext;
		//mCrimes=new ArrayList<Crime>();-создание колекции с элементов Crime
		mSerializer=new CriminalIntentJSONSerializer(mAppContext, FILENAME);
		try{
			mCrimes=mSerializer.loadCrimes();
			}
		catch(Exception e){
			mCrimes=new ArrayList<Crime>();
			Log.e(TAG,"Error loading crimes: ",e);
		}
		
	}
	public boolean saveCrimes()
	{
		try{
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG,"crimes save to file");
			return true;
			} catch(Exception e)
			{Log.e(TAG,"Error saving crimes: ",e);
				return false;}
		
		
	}
	public static CrimeLab get(Context c)
	{
		if(sCrimeLab==null)
		{
			sCrimeLab=new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	public ArrayList<Crime> getCrimes()
	{
		return mCrimes;
	}
	
	public Crime getCrime(UUID id)
	{
		for(Crime c: mCrimes)
		{
			if(c.getId().equals(id)) return c;
		}
		return null;
	}
	public void addCrime(Crime c)
	{
		mCrimes.add(c);
	}
	public void deleteCrime(Crime c)
	{
		mCrimes.remove(c);
	}
	
	
}
