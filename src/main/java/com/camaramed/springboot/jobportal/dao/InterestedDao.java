package com.camaramed.springboot.jobportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camaramed.springboot.jobportal.entity.Interested;

/**
 * @author amayd
 *
 */
public interface InterestedDao extends JpaRepository<Interested, Integer> {

	// List<Interested> findByJobId(int jobId);

}
