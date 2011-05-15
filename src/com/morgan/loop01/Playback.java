package com.morgan.loop01;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

class Playback {
	public File fileName;
	
	Playback(File file) {
		fileName = file;
	}
	
	public void play() {
        // Get the length of the audio stored in the file (16 bit so 2 bytes per short)
        // and create a short array to store the recorded audio.
        int musicLength = (int)(fileName.length()/2);
        short[] music = new short[musicLength];


//        try {
		  // Create a DataInputStream to read the audio data back from the saved file.
		  InputStream is;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		  BufferedInputStream bis = new BufferedInputStream(is);
		  DataInputStream dis = new DataInputStream(bis);
		
		  // Read the file into the music array.
		  int i = 0;
		  try {
			while (dis.available() > 0) {
			    music[i] = dis.readShort();
			    i++;
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		  // Close the input streams.
		  try {
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}     
		
		
		  // Create a new AudioTrack object using the same parameters as the AudioRecord
		  // object used to create the file.
		  AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
		                                         11025, 
		                                         AudioFormat.CHANNEL_CONFIGURATION_MONO,
		                                         AudioFormat.ENCODING_PCM_16BIT, 
		                                         musicLength, 
		                                         AudioTrack.MODE_STREAM);
		  // Start playback
		  audioTrack.play();
		
		  // Write the music buffer to the AudioTrack object
		  audioTrack.write(music, 0, musicLength);
//        }
	}
}