package com.blog.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping("/blogcontroller")
public class BlogController {

	String id;
	String email;
	String userName;

	@Autowired
	BlogDao blogDao;

	@Autowired
	private EmpDao empDao;

	
	
	
	@RequestMapping(value = "/blog", method = RequestMethod.POST)
	public String createBlog(@ModelAttribute("blog") Blog blog, ModelMap mm, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			Object emailFromSessionObject = session.getAttribute("email");
			String emailFromSession = emailFromSessionObject.toString();
			Object userNameFromSessionObject = session.getAttribute("username");
			String userNameFromSession = userNameFromSessionObject.toString();
			this.userName = userNameFromSession;
			this.email = emailFromSession;

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {

				return "you are not login";

			} else {
				String title = blog.getTitle();

				String postcontent = blog.getPostcontent();

				Date createdOnDate = blog.getCreatedOnDate();
				Date lastUpdatedOnDate = blog.getLastUpdatedOnDate();

				if (BlogValidator.validateTitlePattern(title.trim())
						&& BlogValidator.validatePostContentPattern(postcontent.trim())) {

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
	public List<Blog> blogList(HttpServletRequest request) {
		try {
			List<Blog> list = blogDao.getBlogList();

			return list;

			
			
			/*
			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {

				return null;

			} else {
				List<Blog> list = blogDao.getBlogList();

				return list;

			}
*/		} catch (Exception e) {
			return null;
		}

	}

	
	
	
	@RequestMapping(value = "like/{postid}")
	public List<Blog> likePost(@PathVariable String postid, HttpServletRequest request, ModelMap modelMap) {

		try {

			HttpSession session = request.getSession(false);
			session.setAttribute("postid", postid);

			Blog blog = blogDao.getBlogByPostId(postid);

			if (blog == null) {
				System.out.println("this postid is invalid");
				return null;
			}

			String emailFromPostId = blog.getEmail();

			// String emailFromSession = blogDao.verfifyBeforeUpdateDelete(request);

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {

				return null;

			} else {

				blog = blogDao.getBlogByPostId(postid);

				String postId = blog.getPostid();
				this.id = postId;

				List<Blog> list = blogDao.likePost(emailFromPostId, id, request);

				return list;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	
	
	@RequestMapping(value = "/blog/{postid}", method = RequestMethod.PUT)
	public String updateBlog(@ModelAttribute("blog") Blog blog, HttpServletRequest request,
			@PathVariable String postid) {
		try {
			Blog blogObjectGeneratedByPostId = blogDao.getBlogByPostId(postid);

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {

				return "login required";

			} else {

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

	
	
	
	@RequestMapping(value = "/blog/{postId}", method = RequestMethod.DELETE)
	public String deleteBlog(@PathVariable String postId, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {

		try {

			Blog blog = blogDao.getBlogByPostId(postId);

			if (blog == null) {
				return "invalid postid";
			}

			String emailFromListForIncomingPostTd = blog.getEmail();

			String emailFromSession = blogDao.verfifyBeforeUpdateDelete(request);

			Object object = blogDao.verifyUser(request);

			if (object == null) {

				return ("login required");

			} else {

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