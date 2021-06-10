package com.camaramed.springboot.jobportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.entity.JobSeeker;

@Repository
public interface JobSeekerDao extends JpaRepository<JobSeeker, Integer>, JpaSpecificationExecutor<JobPosting> {

}
