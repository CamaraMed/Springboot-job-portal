package com.camaramed.springboot.jobportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.specification.JobPostingSpecification;

@RunWith(SpringRunner.class)
@DataJpaTest
class JobSeekerDaoTest {

	@Autowired
	CompanyDao companyDao;

	@Autowired
	JobPostingDao jobPostingDao;

	Company company;

	@BeforeEach
	void Init() {
		company = new Company("FST", "Tunis", "fst@gmail.com", "3654");
		JobPosting jobPosting1 = new JobPosting(company, 1, "Teacher", "teaching", "no prerequis", "sfax", null, null);

		JobPosting jobPosting2 = new JobPosting(company, 2, "English professor", "teaching", "no prerequis", "tunis",
				null, null);
		List<JobPosting> listJobs = Arrays.asList(jobPosting1, jobPosting2);
		company.setCompanyJobs(listJobs);
		Company savedCompany = companyDao.save(company);
		;
	}

	@Test
	void search_with_one_spec() {
		List<JobPosting> jobs = jobPostingDao.findAll(JobPostingSpecification.likeTitle("prof"));
		assertNotNull(jobs);
		assertEquals(1, jobs.size());
	}

	@Test
	void search_with_all_specs() {
		Specification<JobPosting> specification = Specification.where(JobPostingSpecification.likeTitle("Teach"))
				.and(JobPostingSpecification.equalLocation("sfax")).and(JobPostingSpecification.likeKewords(null));
		List<JobPosting> jobs = jobPostingDao.findAll(specification);
		assertNotNull(jobs);
		assertEquals(1, jobs.size());
	}

	/*
	 * @Test void search_using_or__shouldWork() { Specification<User> specs =
	 * Specification.where(UserSpecifications.likeFirstName("joe"))
	 * .or(UserSpecifications.likeFirstName("yaq"));
	 * 
	 * List<User> users = userRepositorySpec.findAll(specs); assertNotNull(users);
	 * assertEquals(1, users.size()); }
	 * 
	 * @Test void search_with_null_spec__shouldWork() { Specification<User> specs =
	 * Specification.where(UserSpecifications.likeFirstName(null))
	 * .and(UserSpecifications.likeLastName("ess"))
	 * .and(UserSpecifications.equalEmail("yaqineessadiki@gmail.com"));
	 * 
	 * List<User> users = userRepositorySpec.findAll(specs); assertNotNull(users);
	 * assertEquals(1, users.size()); }
	 * 
	 * @Test void search_with_one_spec_no_data__shouldWork() { Specification<User>
	 * specs = Specification.where(UserSpecifications.likeFirstName("joe"));
	 * 
	 * List<User> users = userRepositorySpec.findAll(specs); assertNotNull(users);
	 * assertEquals(0, users.size()); }
	 */

	@AfterEach
	void tearDown() {
		companyDao.delete(company);

	}

}
