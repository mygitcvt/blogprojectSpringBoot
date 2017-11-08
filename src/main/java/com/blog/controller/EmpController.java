package com.blog.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blog.dal.BlogDao;
import com.blog.dal.BlogDaoImpl;
import com.blog.dal.EmpDao;
import com.blog.dal.EmpDaoImpl;
import com.blog.model.Emp;
import com.blog.validation.EmpValidator;

@RestController
@RequestMapping("/empcontroller")
public class EmpController {
	private static final Logger logger = LoggerFactory.getLogger(EmpController.class);

	int id;

	@Autowired
	EmpDao empDao;

	@Autowired
	BlogDao blogDao;

	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	public String saveEmpData(@ModelAttribute("emp") Emp emp, ModelMap modelMap, Errors errors,
			HttpServletResponse response) {
		try {

			String name = emp.getFirst_Name();
			logger.info(name);
			logger.info("just into try");
			if (EmpValidator.validateFirstName(emp.getFirst_Name().trim())
					&& EmpValidator.validateLastName(emp.getLast_Name().trim())
					&& EmpValidator.validateEmail(emp.getEmail().trim())
					&& EmpValidator.validateUsername(emp.getUsername().trim())
					&& EmpValidator.validatePassword(emp.getPassword().trim())) {
				logger.info("before save method");
				empDao.save(emp);
				return "data inserted successfully";

			} else {

				return "record not save because data entered is not matching pattern";
			}

		} catch (Exception e) {
			String error = e.toString();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return error;
		}
	}

	@RequestMapping(value = "/emp", method = RequestMethod.GET)
	public List<Emp> userList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		try {

			// Object checkingUservalidity = blogDao.verifyUser(request);

			/*
			 * if (checkingUservalidity == null) { logger.info("login required");
			 * 
			 * return null;
			 * 
			 * }
			 */

			List<Emp> employeeList = empDao.getEmployees();
			return employeeList;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			e.printStackTrace();
			return null;

		}

	}

	@RequestMapping(value = "/emp", method = RequestMethod.PUT)
	public String updateUser(@RequestBody Emp emp, HttpServletRequest request, HttpServletResponse response) {
		try {

			Object checkingUservalidity = blogDao.verifyUser(request);
			if (checkingUservalidity == null) {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				return "unauthorised access";
			} else {

				HttpSession sessionObjectForEmail = request.getSession(false);
				Object sessionObject = sessionObjectForEmail.getAttribute("email");
				String emailFromSession = sessionObject.toString();

				Emp empObject = empDao.getEmpByemail(emailFromSession);
				int idforlgoinUser = empObject.getId();

				this.id = idforlgoinUser;

				String username = emp.getFirst_Name();
				HttpSession session = request.getSession(false);
				session.setAttribute("username", username);
				Emp empObjectById = empDao.getEmpById(id);
				String first_name = emp.getFirst_Name();

				int gettingIdFromEmpObject = empObjectById.getId();
				if (EmpValidator.validateFirstName(emp.getFirst_Name().trim())
						&& EmpValidator.validateLastName(emp.getLast_Name().trim())
						&& EmpValidator.validateEmail(emp.getEmail().trim())
						&& EmpValidator.validateUsername(emp.getUsername().trim())) {

					empDao.update(emp, gettingIdFromEmpObject);

					return "data updated successfully";
				}
			}

			return "not updated beacuse entered pattern not matching";
		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return "unauthorised access";
		}

	}

	@RequestMapping(value = "/emp", method = RequestMethod.DELETE)
	public String deleteUser(HttpServletRequest request, HttpServletResponse response) {

		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return "unauthorised access";

			}
			HttpSession sessionObjectForEmail = request.getSession(false);
			Object sessionObject = sessionObjectForEmail.getAttribute("email");
			String emailFromSession = sessionObject.toString();

			Emp empObject = empDao.getEmpByemail(emailFromSession);
			int idforlgoinUser = empObject.getId();

			empDao.delete(idforlgoinUser);
			logger.info("record deleted successfully");
			HttpSession session = request.getSession(false);

			session.invalidate();
			return "record deleted successfully ";

		} catch (Exception e) {
			// String error = e.toString();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return "unauthorised access";
		}

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute(value = "emp") Emp emp, ModelMap mm, HttpServletRequest request) {

		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity != null) {

				HttpSession session = request.getSession(false);
				Object sessionObject = session.getAttribute("email");
				String emailFromSession = sessionObject.toString();
				if (emailFromSession.equals(emp.getEmail()))

					return "you are already login";

			}

			String email = emp.getEmail();
			String pwrd = emp.getPassword();
			Emp empnew = null;

			empnew = empDao.getEmployees(emp.getEmail(), emp.getPassword());

			String username = empnew.getFirst_Name();
			String emailGettingFromDataBase = empnew.getEmail();

			if (empnew != null && empnew.getEmail().equals(email) && empnew.getPassword().equals(pwrd)) {

				HttpSession session = request.getSession(true);
				session.setAttribute("uservalidity", true);

				session.setAttribute("username", username);
				session.setAttribute("email", emailGettingFromDataBase);

				return "you are successfully logged in!!";

			} else {
				return "unathorised access";
			}

		} catch (Exception e) {
			String error = e.toString();

			return "unathorised access";
		}

	}

	@RequestMapping(value = "/logout")
	public String Logout(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		try {

			Object checkingUservalidity = blogDao.verifyUser(request);

			if (checkingUservalidity == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return "unauthorised access";

			} else {

				HttpSession session = request.getSession(false);

				session.invalidate();
				HttpSession sessionobject = request.getSession(false);

				return "you are log out";
			}

		} catch (Exception e) {
			// String error = e.toString();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return "unauthorised access";
		}

	}

	/*
	 * @RequestMapping("/showViewEmpathomepage") public ModelAndView
	 * viewEmployeeListAthomepage(HttpServletRequest request) {
	 * 
	 * try {
	 * 
	 * List<Emp> usersList = empDao.getEmployees(); return new
	 * ModelAndView("homeviewemp", "list", usersList); } catch (Exception e) {
	 * return new ModelAndView("redirect:/showViewEmpathomepage");
	 * 
	 * }
	 * 
	 * }
	 */
}