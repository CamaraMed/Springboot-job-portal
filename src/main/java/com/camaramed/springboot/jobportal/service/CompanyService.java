package com.camaramed.springboot.jobportal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camaramed.springboot.jobportal.dao.CompanyDao;
import com.camaramed.springboot.jobportal.dao.JobPostingDao;
import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;

@Service
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private JobPostingDao jobPostingDao;

	@Autowired
	private EntityManager entityManager;

	public List<String> PasswordLookUp(String emailid) {

		Query query = entityManager.createQuery("SELECT password FROM Company c WHERE c.companyUser = :emailId ");
		query.setParameter("emailId", emailid);
		List<String> list = new ArrayList<String>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			String pwd = (String) iterator.next();
			System.out.println(pwd + " is the password");
			list.add(pwd);
		}
		System.out.println("list :::::::::::::::::::::::::::::       " + list);
		return list;
	}

	public void verify(Company c) {
		Company c1 = getCompanyById(c.getCompanyId());
		c1.setVerified(c.isVerified());
		try {
			if (c != null) {
				entityManager.merge(c1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Integer> getCompanyIdFromEmail(String emailid) {

		Query query = entityManager.createQuery("SELECT companyId FROM Company c WHERE c.companyUser = :emailId ");
		query.setParameter("emailId", emailid);
		List<Integer> list = new ArrayList<Integer>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			int cid = (int) iterator.next();
			list.add(cid);
		}
		return list;
	}

	public Company createCompany(Company com) throws Exception {

		Company newCompany = new Company();

		try {
			newCompany = companyDao.save(com);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newCompany;
	}

	public Company getCompanyById(Integer id) {

		// return companyDao.findById(id).get();
		// return companyDao.findById(id).orElse(new Company());
		return companyDao.findById(id).orElseThrow(IllegalArgumentException::new);

	}

	// public Company updateCompany(Company js);

	// public Company getCompany(int id);

	// public void verify(Company c);

	public List<JobPosting> getJobsByCompany(Company company) {

		// List<Company> companyJobsList = jobPostingDao.findByCompany(company);
		List<JobPosting> companyJobsList = new ArrayList<>();
		for (JobPosting c : jobPostingDao.findByCompany(company)) {
			companyJobsList.add(c);
		}
		return companyJobsList;
	}

	// public List<Integer> getCompanyIdFromEmail(String emailid);

	/*
	 * public Optional<Company> getCompanyById(Integer id) { return
	 * companyDao.findById(id); }
	 */
	public Collection<Company> findAllCompanies() {
		List<Company> companies = new ArrayList<>();
		for (Company company : companyDao.findAll()) {
			companies.add(company);
		}
		return companies;
	};

	public Company updateCompany(Company js) {
		Company c = getCompanyById(js.getCompanyId());
		c.setCompanyName(js.getCompanyName());
		c.setCompanyUser(js.getCompanyUser());
		c.setDescription(js.getDescription());
		c.setHeadquarters(js.getHeadquarters());
		c.setVerified(js.isVerified());
		try {
			if (c != null) {
				entityManager.merge(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

}
