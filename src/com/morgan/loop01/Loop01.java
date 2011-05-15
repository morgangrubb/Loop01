package com.morgan.loop01;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.morgan.loop01.AppLog;

public class Loop01 extends Activity {
	private Recorder recorderInstance = new Recorder();
	private Thread recorderThread = new Thread(recorderInstance);
	
	private Playback playbackInstance = new Playback();
	private Thread playbackThread = new Thread(playbackInstance);
	
	private boolean isRecording = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		((Button)findViewById(R.id.btnStartStop)).setOnClickListener(startStopButton);
		((Button)findViewById(R.id.btnPlay)).setOnClickListener(playButton);
	}
	
	private void startRecording() {
//		recorderInstance = new Channel();
		recorderInstance.setFileName(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loop01.raw"));
		recorderThread.start();
		recorderInstance.setRecording(true);

	}
	
	private void stopRecording() {
		recorderInstance.setRecording(false);
		try {
			recorderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void startPlaying() {
		playbackInstance.setFileName(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loop01.raw"));
		playbackThread.start();
		playbackInstance.setPlaying(true);
	}
	
	private View.OnClickListener startStopButton = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (isRecording) {
				stopRecording();
				isRecording = false;
			}
			else {
				startRecording();
				isRecording = true;
			}
			
		}
	};
	
	private View.OnClickListener playButton = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startPlaying();
			
		}
	};
}
