package com.kaamaaya.awsoperations.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="awsdata")
public class Awsdata {

	@Id
	@Column(name="FILENAME")
	private String filename;
	
	@Column(name="FILEURL")
	private String fileurl;

	public Awsdata()
	{
		
	}
	
	
	public Awsdata(String filename, String fileurl) {
		
		this.filename = filename;
		this.fileurl = fileurl;
	}


	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}


	@Override
	public String toString() {
		return "Awsdata [filename=" + filename + ", fileurl=" + fileurl + "]";
	}
	
	
	
}
