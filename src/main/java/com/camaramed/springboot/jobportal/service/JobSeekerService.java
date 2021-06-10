package com.camaramed.springboot.jobportal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camaramed.springboot.jobportal.dao.JobSeekerDao;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.entity.JobPostingsView;
import com.camaramed.springboot.jobportal.entity.JobSeeker;

@Transactional
@Service
public class JobSeekerService {

	@Autowired
	private JobSeekerDao jobSeekerDao;

	@Autowired
	private EntityManager em;

	/*
	 * public List<?> filterJobs(JobPostingsView jpv, List<?> jobIds);
	 * 
	 * public JobSeeker updateJobSeeker(JobSeeker js);
	 * 
	 * public JobSeeker getJobSeeker(int id);
	 * 
	 * public List<String> PasswordLookUp(String emailid);
	 * 
	 * public void verify(JobSeeker j);
	 * 
	 * public List<?> searchJobs(String searchString);
	 * 
	 * public List<Integer> getUserIdFromEmail(String emailid);
	 */

	public List<?> filterJobs(JobPostingsView jpv, List<?> jobIds) {
		boolean locationFlag = false, companyFlag = false, salaryFlag = false;
		String selectQuery = "SELECT jobId, title, description, responsibilites, location, salary, companyId, state, companyName FROM JobPostingsView jpv WHERE jobId in :jobIds";
		String[] salaries = null;
		if (null != jpv.getLocation()) {
			locationFlag = true;
			selectQuery = selectQuery.concat(" AND jpv.location IN (:locations) ");
		}
		if (null != jpv.getCompanyName()) {
			companyFlag = true;
			selectQuery = selectQuery.concat(" AND jpv.companyName IN (:companies) ");
		}
		if (null != jpv.getSalary()) {
			salaryFlag = true;
			selectQuery = selectQuery.concat(" AND jpv.salary >= :salary1 AND jpv.salary <= :salary2 ");
		}
		Query query = em.createQuery(selectQuery);
		query.setParameter("jobIds", jobIds);
		if (locationFlag) {
			String[] location = jpv.getLocation().split(",");
			List<String> locationList = Arrays.asList(location);
			query.setParameter("locations", locationList);
		}
		if (companyFlag) {
			String[] companyArray = jpv.getCompanyName().split(",");
			List<String> companyList = Arrays.asList(companyArray);
			query.setParameter("companies", companyList);
		}
		if (salaryFlag) {
			salaries = jpv.getSalary().split(",");
			query.setParameter("salary1", salaries[0]);
			query.setParameter("salary2", salaries[1]);
		}
		List<?> jpListRes = query.getResultList();

		return jpListRes;

	}

	public List<?> searchJobs(String searchString) {
		searchString = "%" + searchString + "%";
		searchString = searchString.replaceAll(" ", "% %");
		String searchStringArray[] = searchString.split(" ");
		String selectQuery = "SELECT jobId FROM JobPostingsView jp";
		if (!searchString.isEmpty()) {
			selectQuery = selectQuery.concat(" WHERE ");
		}

		for (int i = 0; i < searchStringArray.length; i++) {
			selectQuery = selectQuery.concat("jp.keywords LIKE :searchParam" + i);
			if (i != searchStringArray.length - 1) {
				selectQuery = selectQuery.concat(" AND ");
			}
		}

		Query query = em.createQuery(selectQuery);
		for (int i = 0; i < searchStringArray.length; i++) {
			query.setParameter("searchParam" + i, searchStringArray[i]);
		}

		List<?> list = query.getResultList();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public JobSeeker createJobSeeker(JobSeeker job) throws Exception {
		JobSeeker jobSeeker = new JobSeeker();
		try {
			jobSeeker = jobSeekerDao.save(job);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobSeeker;
	}

	public List<JobPosting> searchJobs(String title, String location) {

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<JobPosting> cQuery = cBuilder.createQuery(JobPosting.class);
		Root<JobPosting> jobRoot = cQuery.from(JobPosting.class);

		Predicate titlePredicate = cBuilder.like(jobRoot.get("title"), "%" + title + "%");

		Predicate locationPredicate = cBuilder.equal(jobRoot.get("location"), location);

		// Predicate companyPredicate = cBuilder.like(jobRoot.get("company"), "%" +
		// company + "%");

		// Predicate kewordsPredicate = cBuilder.like(jobRoot.get("keywords"), "%" +
		// keywords + "%");

		// apply predication
		cQuery.where(titlePredicate, locationPredicate);

		// return Results
		TypedQuery<JobPosting> query = em.createQuery(cQuery);
		return query.getResultList();

	}

	public JobSeeker getJobSeeker(Integer jobSeekerId) {
		return jobSeekerDao.getOne(jobSeekerId);
	}

	public JobSeeker updateJobSeeker(JobSeeker js) {
		JobSeeker jobseeker = getJobSeeker(js.getJobseekerId());
		jobseeker.setEmailId(js.getEmailId());
		jobseeker.setFirstName(js.getFirstName());
		jobseeker.setLastName(js.getLastName());
		jobseeker.setHighestEducation(js.getHighestEducation());
		jobseeker.setPassword(js.getPassword());
		jobseeker.setSkills(js.getSkills());
		jobseeker.setWorkEx(js.getWorkEx());
		try {
			if (jobseeker != null) {
				em.merge(jobseeker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobseeker;
	}

	public List<String> PasswordLookUp(String emailid) {

		Query query = em.createQuery("SELECT password FROM JobSeeker js WHERE js.emailId = :emailId");
		query.setParameter("emailId", emailid);
		List<String> list = new ArrayList<String>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			String pwd = (String) iterator.next();
			list.add(pwd);
		}
		return list;
	}

	public void verify(JobSeeker j) {
		JobSeeker jobseeker = getJobSeeker(j.getJobseekerId());
		jobseeker.setVerified(true);
		try {
			if (jobseeker != null) {
				em.merge(jobseeker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Integer> getUserIdFromEmail(String emailid) {

		Query query = em.createQuery("SELECT jobseekerId FROM JobSeeker js WHERE js.emailId = :emailId");
		query.setParameter("emailId", emailid);
		List<Integer> list = new ArrayList<Integer>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			int uid = (int) iterator.next();
			list.add(uid);
		}
		return list;
	}
}