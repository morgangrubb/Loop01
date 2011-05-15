package com.morgan.loop01;

import java.io.File;

import android.media.AudioFormat;

class Source implements Runnable {

	protected int frequency;
	protected int channelConfiguration;
	protected File fileName;
	protected final Object mutex = new Object();

	// Changing the sample resolution changes sample type. byte vs. short.
	protected static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	
	Source() {
		super();

		AppLog.log("Creating Source()");
		this.setFrequency(11025);
		this.setChannelConfiguration(AudioFormat.CHANNEL_CONFIGURATION_MONO);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	public void setFileName(File fileName) {
		this.fileName = fileName;
	}

	public File getFileName() {
		return fileName;
	}

	/**
	* @param frequency
	*            the frequency to set
	*/
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	* @return the frequency
	*/
	public int getFrequency() {
		return frequency;
	}

	/**
	* @param channelConfiguration
	*            the channelConfiguration to set
	*/
	public void setChannelConfiguration(int channelConfiguration) {
		this.channelConfiguration = channelConfiguration;
	}

	/**
	* @return the channelConfiguration
	*/
	public int getChannelConfiguration() {
		return channelConfiguration;
	}

	/**
	* @return the audioEncoding
	*/
	public int getAudioEncoding() {
		return audioEncoding;
	}
}
