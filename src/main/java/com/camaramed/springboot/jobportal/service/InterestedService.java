package com.camaramed.springboot.jobportal.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camaramed.springboot.jobportal.dao.InterestedDao;
import com.camaramed.springboot.jobportal.entity.Interested;

@Transactional
@Service
public class InterestedService {

	@Autowired
	InterestedDao interestedDao;

	@Autowired
	private EntityManager entityManager;

	public Interested createInterest(Interested c) throws Exception {
		try {
			entityManager.persist(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public Interested getInterest(int id) {
		Interested is = null;

		is = entityManager.find(Interested.class, id);

		return is;
	}

	public boolean deleteInterest(int id) {
		Interested i = getInterest(id);
		if (i != null) {
			entityManager.remove(i);
		} else {
			return false;
		}
		return true;
	}

	public List<?> getInterestedJobId(int jobId, int userId) {
		Query query = entityManager
				.createQuery("SELECT ID FROM Interested jd WHERE jd.jobId = :jobid and jd.jobSeekerId =:userid");
		query.setParameter("jobid", jobId);
		query.setParameter("userid", userId);
		List<?> querylist = query.getResultList();
		return querylist;
	}

	public List<Integer> getAllInterestedJobId(int userId) {
		Query query = entityManager.createQuery("SELECT jobId FROM Interested jd WHERE jd.jobSeekerId =:userid");
		query.setParameter("userid", userId);
		List<Integer> querylist = query.getResultList();
		return querylist;
	}

}
