package com.blog.dal;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

import com.blog.model.Blog;

import com.blog.model.Blog;

@Repository
public class BlogDaoImpl implements BlogDao {

	@Autowired
	MongoTemplate mongoTemplate;
	int noOfLikes;

	public void createblog(String title, String postcontent, String email, String userName, Date createdOnDate,
			Date lastUpdatedOnDate) {

		Blog blogObject = new Blog();
		ObjectId objectId = new ObjectId();

		blogObject.setPostid(objectId.toString());

		blogObject.setEmail(email);
		blogObject.setTitle(title);
		blogObject.setPostcontent(postcontent);
		blogObject.setLikedByUsers(new ArrayList<String>());
		blogObject.setUserName(userName);
		blogObject.setCreatedOnDate(createdOnDate);
		blogObject.setLastUpdatedOnDate(lastUpdatedOnDate);

		mongoTemplate.save(blogObject);

	}

	public int likePost(String email, String postid, HttpServletRequest request) {
		// List<Blog> listofBlogbeforelike = getBlogList();

		Query query = new Query();

		query.addCriteria(Criteria.where("postid").is(postid));

		Blog blogObjectGeneratedByLikePost = mongoTemplate.findOne(query, Blog.class);

		if (blogObjectGeneratedByLikePost != null) {

			HttpSession session = request.getSession(false);
			Object sessionObjectGeneratedFromEmailSetAtLoginTime = session.getAttribute("email");
			String emailFromSession = sessionObjectGeneratedFromEmailSetAtLoginTime.toString();

			if (blogObjectGeneratedByLikePost.getLikedByUsers().contains(emailFromSession)) {
				int noOfLikes = blogObjectGeneratedByLikePost.getNoOfLikes();
				this.noOfLikes = noOfLikes;

			} else {

				blogObjectGeneratedByLikePost.getLikedByUsers().add(emailFromSession);

				blogObjectGeneratedByLikePost.setNoOfLikes(blogObjectGeneratedByLikePost.getNoOfLikes() + 1);

				int noOfLikes = blogObjectGeneratedByLikePost.getNoOfLikes();
				this.noOfLikes = noOfLikes;
				List<String> likedByUsers = blogObjectGeneratedByLikePost.getLikedByUsers();

				if (updateBlogForNoOfLikes(noOfLikes, likedByUsers, postid)) {

					return noOfLikes;

				}

			}

		}

		return noOfLikes;
	}

	public Boolean updateBlogForNoOfLikes(int noOfLikes, List<String> likedByUsers, String postId) {

		try {

			Query query = new Query(Criteria.where("postid").is(postId));

			Update update = new Update();

			update.set("noOfLikes", noOfLikes);

			update.set("likedByUsers", likedByUsers);

			mongoTemplate.updateMulti(query, update, Blog.class);

			return true;

		} catch (Exception e) {

			return false;
		}

	}

	public Blog getBlogByPostId(String postid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("postid").is(postid));

		Blog blog = mongoTemplate.findOne(query, Blog.class);

		return blog;
	}

	public Blog getBlogByEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		Blog blog = mongoTemplate.findOne(query, Blog.class);
		return blog;

	}

	public Boolean update(String postId, Blog blog) {

		try {

			Query query = new Query(Criteria.where("postid").is(postId));

			Update update = new Update();

			update.set("title", blog.getTitle().trim());

			update.set("postcontent", blog.getPostcontent().trim());
			update.set("lastUpdatedOnDate", blog.getLastUpdatedOnDate());
			mongoTemplate.updateFirst(query, update, Blog.class);

			return true;

		} catch (Exception e) {

			return false;
		}

	}

	public Boolean EmailAndUserNameUpdation(String postId, String username,Blog blog) {
		try {

			Query query = new Query(Criteria.where("postid").is(postId));

			Update update = new Update();

			
			update.set("userName", username);
			update.set("lastUpdatedOnDate", blog.getLastUpdatedOnDate());
			mongoTemplate.updateFirst(query, update, Blog.class);

			return true;

		} catch (Exception e) {

			return false;
		}

	}

	public Boolean deleteBlog(String postId) {

		try {

			Query query = new Query(where("postid").is(postId));

			mongoTemplate.remove(query, Blog.class);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public List<Blog> getBlogList() {

		List<Blog> listOfBlog = mongoTemplate.findAll(Blog.class);

		return listOfBlog;

	}

	public Object verifyUser(HttpServletRequest request) {
		Object object = null;

		HttpSession session = request.getSession(false);
		if (session == null) {
			return object;

		} else {

			Object sessionObjectForCheckingUserValidity = session.getAttribute("uservalidity");
			object = sessionObjectForCheckingUserValidity;

			return object;
		}

	}

	public String verfifyBeforeUpdateDelete(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		Object sessionObjectGeneratedFromEmailSetAtLoginTime = session.getAttribute("email");
		String emailFromSession = sessionObjectGeneratedFromEmailSetAtLoginTime.toString();

		return emailFromSession;

	}

}