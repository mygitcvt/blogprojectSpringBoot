package com.blog.dal;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.blog.model.Blog;

public interface BlogDao {

	void createblog(String title, String postcontent, String email, String userName, Date createdOnDate,
			Date lastUpdatedOnDate);

	int likePost(String email, String postid, HttpServletRequest request);

	Blog getBlogByPostId(String postid);

	Blog getBlogByEmail(String email);

	Boolean update(String postId, Blog blog);

	Boolean deleteBlog(String postId);

	List<Blog> getBlogList();

	Object verifyUser(HttpServletRequest request);

	String verfifyBeforeUpdateDelete(HttpServletRequest request);

}