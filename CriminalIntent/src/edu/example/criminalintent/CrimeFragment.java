package edu.example.criminalintent;

import java.util.Date;
import java.util.UUID;






















import edu.example.criminalintent.CrimeListFragment.Callbacks;
import android.annotation.TargetApi;
import android.app.Activity;
//import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment 
{
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private Button mSuspectButton;
	private Callbacks mCallbacks;
	//Context cont;
	/*
	 * обязательный интерфейс для активности-хоста
	 */
	public interface Callbacks
	{
		void onCrimeUpdated(Crime crime);
	}
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mCallbacks=(Callbacks)activity;
	}
	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks=null;
	}
	
	/**
	 * 
	 */
	private static final String TAG="CrimeFragment";
	public static final String EXTRA_CRIME_ID="edu.example.criminalintent.crime_id";
	private static final String DIALOG_DATE="date"; 
	private static final int REQUEST_DATE=0;
	private static final int REQUEST_PHOTO=1;
	private static final int REQUEST_CONTACT=2;
	private static final String DIALOG_IMAGE="image";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UUID crimeId=(UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		//mCrime=new Crime();
		//cont=getActivity();
		UUID crimeId=(UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
		setHasOptionsMenu(true);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch(item.getItemId())
		{
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity())!=null)
			{
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.fragment_crime, container,false);
		/*
		 * проверка для подвязки кнопки назад на фрагмент!
		 */
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
		{
			if(NavUtils.getParentActivityName(getActivity())!=null)
			{
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
	
		mTitleField=(EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence c,int start,int before,int count){
				mCrime.setTitle(c.toString());
				mCallbacks.onCrimeUpdated(mCrime);
				getActivity().setTitle(mCrime.getTitle());
			}
			public void beforeTextChanged(CharSequence c,int start,int count,int after){
				
			}
			public void afterTextChanged(Editable c){
				
			}
		});
		mDateButton=(Button) v.findViewById(R.id.crime_date);
		updateDate();
		//mDateButton.setText(mCrime.getmDate().toString());
		//mDateButton.setEnabled(false);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm=getActivity().getSupportFragmentManager();
				//DatePickerFragment dialog=new DatePickerFragment();
				DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
				
			}
		});
		
		mSolvedCheckBox=(CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.ismSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setmSolved(isChecked);
				mCallbacks.onCrimeUpdated(mCrime);
				
			}
		});
		mPhotoButton=(ImageButton) v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent i=new Intent(getActivity(),CrimeCameraActivity.class);
				//startActivity(i);
				startActivityForResult(i, REQUEST_PHOTO);
				
			}
		});
		/*
		 * Если камеры нету, заблокировать работу с нею
		 */
		PackageManager pm=getActivity().getPackageManager();//hasSystemFeature(PackageManager.FEATURE_CAMERA)-функции устройства
		if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
		{
			mPhotoButton.setEnabled(false);
		}
		mPhotoView =(ImageView) v.findViewById(R.id.crime_imageView1);
		mPhotoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Photo p=mCrime.getPhoto();
				if(p==null)
					return;
				FragmentManager fm=getActivity().getSupportFragmentManager();//вызов через фрагментменеджер
				String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			
				
			}
		});
		Button reportButton=(Button)v.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{	
				//создаем неявный интент для отправки сообщений
				Intent i=new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				i=Intent.createChooser(i, getString(R.string.send_report));//отправка сообщения взятого из ресурса-стринг-сенд-репорт
				startActivity(i);
			}
		});
		//выбор кому позвонить из списка контактов телефона
		mSuspectButton=(Button) v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) 
			{
				Intent i=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, REQUEST_CONTACT);				
			}
		});
		if(mCrime.getSuspect()!=null)
		{
			mSuspectButton.setText(mCrime.getSuspect());
		}
		return v;
		
	}
	public static CrimeFragment newInstance(UUID crimeId)
	{
		Bundle args=new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment fragment=new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
			if(resultCode!=Activity.RESULT_OK)return;
			if(requestCode==REQUEST_DATE)
			{
				Date date=(Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
				mCrime.setmDate(date);
				mCallbacks.onCrimeUpdated(mCrime);
				//mDateButton.setText(mCrime.getmDate().toString());
				updateDate();
			}
			else if(requestCode==REQUEST_PHOTO)
			{
				//создание нового Фото и связываем с Сrime
				String filename=data.getStringExtra(CrimeCameaFragment.EXTRA_PHOTO_FILENAME);
				if(filename!=null)
				{
					Photo p=new Photo(filename);
					mCrime.setPhoto(p);
					mCallbacks.onCrimeUpdated(mCrime);
					//Log.i(TAG,"Crime: "+mCrime.getTitle()+" has photo");
					showPhoto();
				}
			}
			else if(requestCode==REQUEST_CONTACT)
			{
				Uri contactUri=data.getData();
				//определение полей, которые вернутся по запросу
				String[]queryFields = new String[]
						{
						ContactsContract.Contacts.DISPLAY_NAME
						};
				Cursor c=getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
				//проверка полученого результата
				if(c.getCount()==0)
				{
					c.close();
					return;
				}
				//извлекаем первый столбец данных
				c.moveToFirst();
				String suspect=c.getString(0);
				mCrime.setSuspect(suspect);
				mCallbacks.onCrimeUpdated(mCrime);
				mSuspectButton.setText(suspect);
				c.close();
			}			
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	
	}

	public void updateDate()
	{
		mDateButton.setText(mCrime.getmDate().toString());
	}
	private void showPhoto()
	{
		//назначение изображения полученого на основе фотографии
		Photo p=mCrime.getPhoto();
		BitmapDrawable b=null;
		if(p!=null)
		{
			String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b=PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}

	@Override
	public void onStart() //желательно выгрузку и загрузку фото делать в онСтарт и онСтоп
	{
		super.onStart();
		showPhoto();	
	}

	@Override
	public void onStop()
	{		
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}
	
	private String getCrimeReport()
	{
		String solvedString=null;
		if(mCrime.ismSolved())
		{
			solvedString=getString(R.string.crime_report_solved);			
		} else
		{
			solvedString=getString(R.string.crime_report_unsolved);
		}
		String dateFormat="EEE,MMM dd";
		String dateString=DateFormat.format(dateFormat, mCrime.getmDate()).toString();
		
		String suspect =mCrime.getSuspect();
		if(suspect==null)
		{
			suspect=getString(R.string.crime_report_no_suspect);
		} else
		{
			suspect=getString(R.string.crime_report_suspect,suspect);
		}
		String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
		return report;
	}
	

}
