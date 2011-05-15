package com.morgan.loop01;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.media.AudioManager;
import android.media.AudioTrack;

class Playback extends Source {

	AudioTrack track;
	private boolean isPlaying = false;

	Playback() {
		super();

		int minSize = AudioTrack.getMinBufferSize(this.getFrequency(), this.getChannelConfiguration(), this.getAudioEncoding());        
		track = new AudioTrack( AudioManager.STREAM_MUSIC, this.getFrequency(), this.getChannelConfiguration(), this.getAudioEncoding(), minSize, AudioTrack.MODE_STREAM);
		track.play();        
	}
	
	@Override
	public void run() {

		// Open output stream…
		if (this.getFileName() == null) {
			throw new IllegalStateException("fileName is null");
		}
		
		BufferedInputStream bufferedStreamInstance = null;

		if (!fileName.exists()) {
			throw new IllegalStateException("File not found: " + this.getFileName().toString());
		}

		try {
			bufferedStreamInstance = new BufferedInputStream(new FileInputStream(this.getFileName()));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Cannot Open File", e);
		}

		DataInputStream dataInputStreamInstance = new DataInputStream(bufferedStreamInstance);

		// We’re important…
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        while(true) {
        	
        	if (this.isPlaying) {
        		short samples[] = new short[1024];
        		
        		for (int readIndex = 0; readIndex < 1024; readIndex++) {
        			try {
						if (dataInputStreamInstance.available() > 0) {
							samples[readIndex] = dataInputStreamInstance.readShort();
						}
						else {
							// Pad it with silence
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        		
        		track.write(samples, 0, 1024);
        	}

           
        }   
	}
	
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

}