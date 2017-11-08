package com.blog.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.blog.model.Emp;

@Repository
public class EmpDaoImpl implements EmpDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public EmpDaoImpl() {

	}

	public int save(Emp p) {

		String sql = "insert into SIGNUP(First_Name,Last_Name,Username,Email,Password)values('"
				+ p.getFirst_Name().trim() + "','" + p.getLast_Name().trim() + "','" + p.getUsername().trim() + "','"
				+ p.getEmail().trim() + "','" + p.getPassword().trim() + "')";
		return jdbcTemplate.update(sql);
	}

	public int update(Emp p, int id) {

		String sql = "update SIGNUP set First_Name='" + p.getFirst_Name().trim() + "', Last_Name='"
				+ p.getLast_Name().trim() + "', Username='" + p.getUsername().trim() + "',Email='" + p.getEmail().trim()
				+ "' where id=" + id + "";
		return jdbcTemplate.update(sql);
	}

	public int delete(int id) {

		String sql = "delete from SIGNUP where id=" + id + "";

		return jdbcTemplate.update(sql);
	}

	public Emp getEmpById(int id) {

		String sql = "select * from SIGNUP where id=?";

		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<Emp>(Emp.class));
	}

	public Emp getEmpByemail(String email) {

		String sql = "select * from SIGNUP where email=?";

		return jdbcTemplate.queryForObject(sql, new Object[] { email }, new BeanPropertyRowMapper<Emp>(Emp.class));

	}

	public List<Emp> getEmployees() {

		return jdbcTemplate.query("select * from SIGNUP", new RowMapper<Emp>() {
			public Emp mapRow(ResultSet rs, int row) throws SQLException {
				Emp e = new Emp();

				e.setId(rs.getInt(1));

				e.setFirst_Name(rs.getString(2));
				e.setLast_Name(rs.getString(3));
				e.setUsername(rs.getString(4));
				e.setEmail(rs.getString(5));
				e.setPassword(rs.getString(6));

				return e;
			}
		});
	}

	public Emp getEmployees(String email, String pwd) {

		return jdbcTemplate.queryForObject("SELECT * FROM SIGNUP WHERE email=? and password=?",
				new Object[] { email, pwd }, new RowMapper<Emp>() {
					public Emp mapRow(ResultSet rs, int row) throws SQLException {

						Emp e = new Emp();
						e.setEmail(rs.getString("Email"));
						e.setPassword(rs.getString("Password"));
						e.setFirst_Name(rs.getString("First_Name"));

						return e;

					}

				});
	}

}