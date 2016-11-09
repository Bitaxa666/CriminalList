package edu.example.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameaFragment extends Fragment
{
	private static final String TAG="CrimeCameraFragment";
	public static final String EXTRA_PHOTO_FILENAME="edu.example.criminalintent.photo_filename";
	private View mProgressConteiner;
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	private Camera.ShutterCallback mShuterCalback=new Camera.ShutterCallback() 
	{
		
		@Override
		public void onShutter() 
		{
			mProgressConteiner.setVisibility(View.VISIBLE);//���������� ������ ������� � �������������
			
		}
	};
	private Camera.PictureCallback mJpegCallback=new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			//������� ��� �����
			String filename=UUID.randomUUID().toString()+".jpg";
			//���������� ������ jpg on Disk
			FileOutputStream os=null;
			boolean success=true;
			try{
				os=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e)
			{
				Log.e(TAG,"ERROR writing to file "+filename,e);
				success=false;
			} finally{
				try{
					if(os!=null)
						os.close();
				} catch (Exception e){
					Log.e(TAG,"ERROR closing file "+filename,e);
					success=false;
				}
			}
			if(success){
				Log.e(TAG,"JPEG saved at  "+filename);
				Intent i=new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, i);
			} else
			{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
			
		}
	};
	
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View v= inflater.inflate(R.layout.fragment_crime_camera, container,false);
		Button takePictureButton=(Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				if(mCamera!=null)
				{
					mCamera.takePicture(mShuterCalback, null, mJpegCallback);
				}							
			}
		});
		mSurfaceView=(SurfaceView) v.findViewById(R.id.crime_camera_surface);
		SurfaceHolder holder=mSurfaceView.getHolder();
		//����� setType and SURFACE_TYPE_PUSH_BUFFERS ���������� �� ����
		//��� � ������� �� ����������� �� 3.0
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//��������������� �������� ����������� � ������
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				//���������� ��������������� ��������
				if(mCamera!=null)
				{
					mCamera.stopPreview();
				}
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) 
			{
				try{
					if(mCamera!=null)
					{
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e)
				{
					Log.e(TAG,"ERROR setings up preview display",e);
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) 
			{
				if(mCamera==null)return;
				//������ ����������� ���������, �������� ������ ������� ���������
				Camera.Parameters parameters=mCamera.getParameters();
				//Size s=null;
				Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);//������� ������� Preview
				parameters.setPreviewSize(s.width, s.height);
				//������ �������� ��� �������� ������� � ������
				s=getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try{
					mCamera.startPreview();//������ ���������
				} catch(Exception e)
				{
					Log.e(TAG, "Could not start preview",e);
					mCamera.release();//������ �������� ������ ������ ������������� �� �������������, ���� � ����������
					mCamera=null;
				}
				
			}
		});
		//������ ���������� ��������� �� ������� �� ������ ����
		mProgressConteiner=v.findViewById(R.id.crime_camera_progressContainer);
		mProgressConteiner.setVisibility(View.INVISIBLE);
	
		return v;
	}
	/*
	 * ������� ����������� ��������� ����������� �������
	 */
	private Size getBestSupportedSize(List <Size> sizes, int width, int height)
	{
		Size bestSize=sizes.get(0);
		int largestArea=bestSize.width * bestSize.height;
		for(Size s : sizes)
		{
			int area=s.width * s.height;
			if(area>largestArea)
			{
				bestSize=s;
				largestArea=area;
			}
		}
		return bestSize;
	}

	
	@TargetApi(9)
	/*
	 * ��������� ������ ������ ��� ������ ������ ����������� � ���������
	 */
	@Override
	public void onResume() 
	{		
		super.onResume();
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD)
		{
			mCamera=Camera.open(0);
		}
		else{
			mCamera=Camera.open();
		}
	}
	/*
	 * ���������� ������ ������
	 */
	@Override
	public void onPause() 
	{	
		super.onPause();
		if(mCamera!=null)//����������� ��������
		{
			mCamera.release();
			mCamera=null;
		}
	}
	
	
	
	
}
