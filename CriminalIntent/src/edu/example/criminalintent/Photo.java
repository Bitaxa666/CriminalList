package edu.example.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo 
{
	private static final String JSON_FILENAME="filename";
	private String mFilenName;
	//создание фото представляющего фаил на диске
	public Photo(String filename)
	{
		mFilenName=filename;
	}
	public Photo (JSONObject json) throws JSONException
	{
		mFilenName=json.getString(JSON_FILENAME);
	}
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json=new JSONObject();
		json.put(JSON_FILENAME, mFilenName);
		return json;
	}
	public String getFilename()
	{
		return mFilenName;
	}
	

}
