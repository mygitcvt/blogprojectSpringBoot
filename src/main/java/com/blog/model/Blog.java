package com.blog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "blog1")
public class Blog {

	@Id
	private String postid;
	private String title;
	private String postcontent;
	private int noOfLikes = 0;
	private String email;
	private String userName;
	private Date createdOnDate = new Date();
	private Date lastUpdatedOnDate = new Date();

	public Date getLastUpdatedOnDate() {
		return lastUpdatedOnDate;
	}

	public void setLastUpdatedOnDate(Date lastUpdatedOnDate) {
		this.lastUpdatedOnDate = lastUpdatedOnDate;
	}

	public Date getCreatedOnDate() {
		return createdOnDate;
	}

	public void setCreatedOnDate(Date createdOnDate) {
		this.createdOnDate = createdOnDate;
	}

	private List<String> likedByUsers = new ArrayList<String>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getLikedByUsers() {
		return likedByUsers;
	}

	public void setLikedByUsers(List<String> likedByUsers) {
		this.likedByUsers = likedByUsers;
	}

	public int getNoOfLikes() {
		return noOfLikes;
	}

	public void setNoOfLikes(int noOfLikes) {
		this.noOfLikes = noOfLikes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPostcontent() {
		return postcontent;
	}

	public void setPostcontent(String postcontent) {
		this.postcontent = postcontent;
	}

}