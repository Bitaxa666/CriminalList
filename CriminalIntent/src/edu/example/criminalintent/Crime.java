package edu.example.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime 
{
	private static final String JSON_ID="id";
	private static final String JSON_TITLE="title";
	private static final String JSON_DATE="date";
	private static final String JSON_SOLVED="solved";
	private static final String JSON_PHOTO="photo";
	private static final String JSON_SUSPECT="suspect";
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;
	
	public Crime()
	{
		mId=UUID.randomUUID();
		mDate=new Date();
	}
	/**
	 * ����� ��� ������� ������ ����������� � json-format
	 * @param json
	 * @throws JSONException
	 */
	public Crime(JSONObject json) throws JSONException
	{
		mId=UUID.fromString(json.getString(JSON_ID));
		mTitle=json.getString(JSON_TITLE);
		mSolved=json.getBoolean(JSON_SOLVED);
		mDate=new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_PHOTO))
			mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
		if(json.has(JSON_SUSPECT))
			mSuspect=json.getString(JSON_SUSPECT);
	}
	/**
	 * ����� ��� ����� ��� ���������� ����������� ��������
	 * @return ������ ����������� ����������
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json=new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED,mSolved);
		json.put(JSON_DATE,mDate.getTime());
		if(mPhoto!=null)
			json.put(JSON_PHOTO, mPhoto.toJSON());
			json.put(JSON_SUSPECT, mSuspect);
		return json;
		
	}

	public Date getmDate()
	{
		
		return mDate;
	}

	public void setmDate(Date mDate) 
	{
		this.mDate = mDate;
	}

	public boolean ismSolved() 
	{
		return mSolved;
	}

	public void setmSolved(boolean mSolved)
	{
		this.mSolved = mSolved;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public UUID getId()
	{
		return mId;
	}
	public Photo getPhoto()
	{
		return mPhoto;
	}
	public void setPhoto(Photo p)
	{
		mPhoto=p;
	}
	public String getSuspect()
	{
		return mSuspect;
	}
	public void setSuspect(String suspect)
	{
		mSuspect=suspect;
	}

	@Override
	public String toString() {
		return mTitle;
	}
	
	
	

}
