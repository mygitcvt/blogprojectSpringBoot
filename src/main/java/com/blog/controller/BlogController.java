package com.blog.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blog.dal.BlogDao;
import com.blog.dal.BlogDaoImpl;
import com.blog.dal.EmpDao;
import com.blog.model.Blog;
import com.blog.model.Emp;
import com.blog.validation.BlogValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/blogcontroller")
public class BlogController {
	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

	String id;
	String email;
	String userName;
	int noOflikes;

	@Autowired
	BlogDao blogDao;

	@Autowired
	private EmpDao empDao;

	@RequestMapping(value = "/blog", method = RequestMethod.POST)
	public String createBlog(@ModelAttribute("blog") Blog blog, ModelMap mm, HttpServletRequest request,
			HttpServletResponse response) {
		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				logger.info("user not login for creating blog");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return "unauthorised access";

			} else {
				logger.info("in else block for creating block");

				HttpSession session = request.getSession(false);
				logger.info("checking session for creating blog{}", session);

				Object emailFromSessionObject = session.getAttribute("email");
				String emailFromSession = emailFromSessionObject.toString();
				Object userNameFromSessionObject = session.getAttribute("username");
				String userNameFromSession = userNameFromSessionObject.toString();
				this.userName = userNameFromSession;
				this.email = emailFromSession;

				// logger.info("checking blog objject{}",blog);
				String title = blog.getTitle();

				logger.info("checking title: {}", title);

				String postcontent = blog.getPostcontent();

				Date createdOnDate = blog.getCreatedOnDate();
				Date lastUpdatedOnDate = blog.getLastUpdatedOnDate();
				logger.info("createdon date : {}", createdOnDate);
				boolean validateTitle = BlogValidator.validateTitlePattern(title.trim());
				logger.info("validate title : {}", validateTitle);

				if (BlogValidator.validateTitlePattern(title.trim())
						&& BlogValidator.validatePostContentPattern(postcontent.trim())) {

					logger.info("creating blog");

					blogDao.createblog(title.trim(), postcontent.trim(), email, userName, createdOnDate,
							lastUpdatedOnDate);

					return "blog created successfully";

				} else {

					return ("blog not created");

				}
			}

		} catch (Exception e) {
			String error = e.toString();

			return error;
		}

	}

	@RequestMapping(value = "/blog", method = RequestMethod.GET)
	public List<Blog> blogList(HttpServletRequest request, HttpServletResponse response) {
		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.info("unauthorised access");
				return null;

			} else {
				List<Blog> list = blogDao.getBlogList();

				return list;

			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

	}

	@RequestMapping(value = "like/{postid}")
	public int likePost(HttpServletRequest request, ModelMap modelMap, @PathVariable String postid,
			HttpServletResponse response) {

		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				logger.info("unauthorised access");

			} else {

				HttpSession session = request.getSession(false);

				Object sessionObject = session.getAttribute("email");
				String emailFromSession = sessionObject.toString();

				int noOfLikes = blogDao.likePost(emailFromSession, postid, request);

				this.noOflikes = noOfLikes;

				return noOfLikes;

			}
			return (Integer) null;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return (Integer) null;

		}

	}

	@RequestMapping(value = "/blog", method = RequestMethod.PUT)
	public String updateBlog(@RequestBody Blog blog, HttpServletRequest request, HttpServletResponse response) {
		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				return "unauthorised access";

			} else {

				HttpSession session = request.getSession(false);

				Object sessionObject = session.getAttribute("email");
				String emailFromSession = sessionObject.toString();

				Blog blogObject = blogDao.getBlogByEmail(emailFromSession);
				String postidFromBlog = blogObject.getPostid();

				Blog blogObjectGeneratedByPostId = blogDao.getBlogByPostId(postidFromBlog);

				if (BlogValidator.validateTitlePattern(blog.getTitle().trim())
						&& BlogValidator.validatePostContentPattern(blog.getPostcontent().trim())) {
					String postId = blogObjectGeneratedByPostId.getPostid();

					this.id = postId;

					blogDao.update(id, blog);

					return "blog updated successfully";

				} else {

					return "can't update blog";

				}

			}
		} catch (Exception e) {
			String error = e.toString();

			return error;

		}

	}

	@RequestMapping(value = "/blog", method = RequestMethod.DELETE)
	public String deleteBlog(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		try {
			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				return ("unauthorised access");

			} else {
				HttpSession session = request.getSession(false);

				Object sessionObject = session.getAttribute("email");
				String emailFromSession = sessionObject.toString();

				Blog blogObject = blogDao.getBlogByEmail(emailFromSession);
				String postId = blogObject.getPostid();
				String emailFromListForIncomingPostTd = blogObject.getEmail();

				if (emailFromListForIncomingPostTd.equals(emailFromSession)) {
					blogDao.deleteBlog(postId);
					return "record deleted successfully";
				}

				else {

					return "record not deleted";

				}

			}
		} catch (Exception e) {
			String error = e.toString();

			return error;

		}

	}
	/*
	 * @RequestMapping(value = "/showbloglistathomepage") public ModelAndView
	 * showBlogList() {
	 * 
	 * List<Blog> list = blogDao.getBlogList(); return new
	 * ModelAndView("homewelcome", "list", list);
	 * 
	 * }
	 */
}