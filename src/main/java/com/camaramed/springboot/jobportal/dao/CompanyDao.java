package com.camaramed.springboot.jobportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camaramed.springboot.jobportal.entity.Company;

/**
 * @author amayd
 *
 */
@Repository
public interface CompanyDao extends JpaRepository<Company, Integer> {

//public interface CompanyDao {

	/*
	 * public List<String> PasswordLookUp(String emailid);
	 * 
	 * public Company createCompany(Company com) throws Exception;
	 * 
	 * public Company updateCompany(Company js);
	 * 
	 * public Company getCompany(int id);
	 * 
	 * public void verify(Company c);
	 * 
	 * public List<?> getJobsByCompany(int companyId);
	 * 
	 * public List<Integer> getCompanyIdFromEmail(String emailid);
	 */

}
