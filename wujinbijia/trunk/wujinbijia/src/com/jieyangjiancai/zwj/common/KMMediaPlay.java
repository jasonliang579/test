package com.jieyangjiancai.zwj.common;

import java.io.IOException;

import android.media.MediaPlayer;

public class KMMediaPlay {
	public static KMMediaPlay instance; 
	private static MediaPlayer mediaPlayer; 
	
	public static KMMediaPlay getInstance(){
		
		if(instance == null){
			instance = new KMMediaPlay();
		}
		return instance;
			
	}
	
	/**
	 * 播放MP3
	 * 
	 * @param path
	 */
	public void play(String path){
		   if (path != null) {
		        try {
		        	stopPlaying();
		        	if(mediaPlayer == null)
		        		mediaPlayer = new MediaPlayer();
					mediaPlayer.setDataSource(path);
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
	}
	
	private void stopPlaying(){
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
