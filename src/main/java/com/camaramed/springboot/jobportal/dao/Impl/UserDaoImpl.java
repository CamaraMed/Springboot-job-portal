package com.camaramed.springboot.jobportal.dao.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camaramed.springboot.jobportal.dao.UserDao;


@Repository
@Transactional
@Service
public class UserDaoImpl implements UserDao {
	@PersistenceContext
	private EntityManager entityManager;
}
