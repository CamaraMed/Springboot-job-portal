package com.camaramed.springboot.jobportal.service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camaramed.springboot.jobportal.dao.JobPostingDao;
import com.camaramed.springboot.jobportal.entity.JobPosting;

@Transactional
@Service
public class JobPostingService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JobPostingDao jobPostingDao;

	public JobPosting getJobPosting(Integer id) {
		return jobPostingDao.findById(id).get();
	}

	public JobPosting createJobPosting(JobPosting j) {
		// TODO Auto-generated method stub
		return jobPostingDao.save(j);
	};

	public boolean deleteJobPosting(Integer id) {
		JobPosting p = getJobPosting(id);
		if (p != null) {
			entityManager.remove(p);
			return true;
		} else {
			return false;
		}

	}

	public JobPosting updateJobPosting(JobPosting p) {

		JobPosting p1 = getJobPosting(p.getJobId());
		p1.setDescription(p.getDescription());
		p1.setLocation(p.getLocation());
		p1.setResponsibilities(p.getResponsibilities());
		p1.setSalary(p.getSalary());
		p1.setState(p.getState());
		p1.setTitle(p.getTitle());
		try {
			if (p1 != null) {
				entityManager.merge(p1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p1;
	}

}
