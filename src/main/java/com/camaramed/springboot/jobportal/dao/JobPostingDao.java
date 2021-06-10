package com.camaramed.springboot.jobportal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;

@Repository
public interface JobPostingDao extends JpaRepository<JobPosting, Integer>, JpaSpecificationExecutor<JobPosting> {

	List<JobPosting> findByCompany(Company company);

}