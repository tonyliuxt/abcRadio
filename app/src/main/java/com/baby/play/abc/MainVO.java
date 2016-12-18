package com.baby.play.abc;

public class MainVO {
	int contentId;
	String mainlabel;
	int backgroundSourceId;
	int imageresourceId;
	String backgroundSourcePath;
	int messageType;
	String name;
	String weatherName;
	
	public String getWeatherName() {
		return weatherName;
	}

	public void setWeatherName(String weatherName) {
		this.weatherName = weatherName;
	}

	public int getContentId(){
		return contentId;
	}
	
	public void setContentId(int inContentid){
		this.contentId = inContentid;
	}
	
	public String getMainLabel(){
		return mainlabel;
	}
	
	public void setMainLabel(String inMainLabel){
		this.mainlabel = inMainLabel;
	}
	
		
	public int getBackgroundSourceId(){
		return backgroundSourceId;
	}
	
	public void setBackgroundSourceId(int inBackgroundSourceId){
		this.backgroundSourceId = inBackgroundSourceId;
	}

	public String getBackgroundSourcePath(){
		return backgroundSourcePath;
	}
	
	public void setBackgroundSourcePath(String inBackgroundSourcePath){
		this.backgroundSourcePath = inBackgroundSourcePath;
	}

	public int getMessageType(){
		return messageType;
	}
	
	public void setMessageType(int inMessageType){
		this.messageType = inMessageType;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String inName){
		this.name = inName;
	}
	public int getImageresourceId() {
		return imageresourceId;
	}

	public void setImageresourceId(int imageresourceId) {
		this.imageresourceId = imageresourceId;
	}

}