package com.blog.dal;

import java.util.List;

import com.blog.model.Emp;

public interface EmpDao {
	
	
	 int save(Emp p);
	 int update(Emp p, int id);
	 int delete(int id);
	 Emp getEmpById(int id);
	 Emp getEmpByemail(String email);
	 List<Emp> getEmployees();
	 Emp getEmployees(String email, String pwd);
	

}