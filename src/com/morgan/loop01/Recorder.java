package com.morgan.loop01;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.morgan.loop01.AppLog;

public class Recorder extends Source {

	private volatile boolean isRecording;

	/**
	*
	*/
	public Recorder() {
		super();
		AppLog.log("Creating Channel()");
//		this.setPaused(false);
	}

	public void run() {
		AppLog.log("Run called");
		// Wait until we’re recording…
		synchronized (mutex) {
			while (!this.isRecording) {
				try {
					AppLog.log("waiting");
					mutex.wait();
					AppLog.log("waiting over");
				} catch (InterruptedException e) {
					throw new IllegalStateException("Wait() interrupted!", e);
				}
			}
		}

		record();
	}

	private void record() {
		// Open output stream…
		if (this.fileName == null) {
			throw new IllegalStateException("fileName is null");
		}
		
		BufferedOutputStream bufferedStreamInstance = null;

		if (fileName.exists()) {
			fileName.delete();
		}

		try {
			fileName.createNewFile();
		} catch (IOException e) {
			throw new IllegalStateException("Cannot create file: " + fileName.toString());
		}

		try {
			bufferedStreamInstance = new BufferedOutputStream(new FileOutputStream(this.fileName));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Cannot Open File", e);
		}

		DataOutputStream dataOutputStreamInstance = new DataOutputStream(bufferedStreamInstance);

		// We’re important…
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		// Allocate Recorder and Start Recording…
		int bufferRead = 0;
		int bufferSize = AudioRecord.getMinBufferSize(this.getFrequency(),
		this.getChannelConfiguration(), this.getAudioEncoding());
		AudioRecord recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, this.getFrequency(), this.getChannelConfiguration(), this.getAudioEncoding(), bufferSize);
		short[] tempBuffer = new short[bufferSize];
		recordInstance.startRecording();
		while (this.isRecording) {
//			// Are we paused?
//			synchronized (mutex) {
//				if (this.isPaused) {
//					AppLog.log("Paused");
//					try {
//						mutex.wait(250);
//					} catch (InterruptedException e) {
//						throw new IllegalStateException("Wait() interrupted!", e);
//					}
//					continue;
//				}
//			}

			bufferRead = recordInstance.read(tempBuffer, 0, bufferSize);
			if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
			} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			}
			try {
				for (int idxBuffer = 0; idxBuffer < bufferRead; ++idxBuffer) {
					dataOutputStreamInstance.writeShort(tempBuffer[idxBuffer]);
				}
			} catch (IOException e) {
				throw new IllegalStateException("dataOutputStreamInstance.writeShort(curVal)");
			}

		}
		AppLog.log("Stopping recording");
		// Close resources…
		recordInstance.stop();
		try {
			bufferedStreamInstance.close();
		} catch (IOException e) {
			throw new IllegalStateException("Cannot close buffered writer.");
		}
	}
	
	/**
	* @param isRecording
	*            the isRecording to set
	*/
	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	/**
	* @return the isRecording
	*/
	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}



//	/**
//	* @param isPaused
//	*            the isPaused to set
//	*/
//	public void setPaused(boolean isPaused) {
//		synchronized (mutex) {
//			this.isPaused = isPaused;
//		}
//	}
//
//	/**
//	* @return the isPaused
//	*/
//	public boolean isPaused() {
//		synchronized (mutex) {
//			return isPaused;
//		}
//	}
}
