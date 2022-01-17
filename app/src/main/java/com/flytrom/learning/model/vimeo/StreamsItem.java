package com.flytrom.learning.model.vimeo;

import com.google.gson.annotations.SerializedName;

public class StreamsItem{

	@SerializedName("profile")
	private String profile;

	@SerializedName("fps")
	private int fps;

	@SerializedName("id")
	private String id;

	@SerializedName("quality")
	private String quality;

	public void setProfile(String profile){
		this.profile = profile;
	}

	public String getProfile(){
		return profile;
	}

	public void setFps(int fps){
		this.fps = fps;
	}

	public int getFps(){
		return fps;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setQuality(String quality){
		this.quality = quality;
	}

	public String getQuality(){
		return quality;
	}
}