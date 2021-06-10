package com.camaramed.springboot.jobportal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.camaramed.springboot.jobportal.dao.CompanyDao;
import com.camaramed.springboot.jobportal.dao.JobPostingDao;
import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;

@RunWith(SpringRunner.class)
@DataJpaTest
class CompanyDaoTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private JobPostingDao jobPostingDao;

	@Autowired
	private CompanyDao companyDao;

	@Test
	void saveCompany() throws Exception {
		Company company = new Company("ISAS", "sfax", "isas@gmail.com", "isas");
		Company savedCompany = testEntityManager.persist(company);
		Company getFromDbCompany = companyDao.findById(savedCompany.getCompanyId()).get();
		assertThat(getFromDbCompany).isEqualTo(savedCompany);
	}

	/*
	 * @Test void testGetByCompany() { JobPosting jobPosting = new
	 * JobPosting(companyDao.getOne(1), 1, "Development",
	 * "this job is for java developers", "no prerequis", "sfax", null, null);
	 * testEntityManager.persist(jobPosting); List<?> companyJobsList =
	 * jobPostingDao.findByCompany(companyDao.getOne(1).getCompanyId());
	 * assertThat(companyJobsList).isNotEmpty(); }
	 */

	@Test
	void testCreateJobPosting() {
		Company company = new Company("FST", "Tunis", "fst@gmail.com", "3654");
		JobPosting jobPosting1 = new JobPosting(company, 1, "Teacher", "teaching", "no prerequis", "sfax", null, null);

		JobPosting jobPosting2 = new JobPosting(company, 2, "English professor", "teaching", "no prerequis", "tunis",
				null, null);
		List<JobPosting> listJobs = Arrays.asList(jobPosting1, jobPosting2);
		company.setCompanyJobs(listJobs);
		Company savedCompany = companyDao.save(company);
		// JobPosting getFromdb = jobPostingDao.getOne(1);
		// List<JobPosting> company1Jobs =
		// jobPostingDao.findByCompany(companyDao.getOne(1));
		Company getCompany = companyDao.findById(savedCompany.getCompanyId()).get();
		// assertThat(getFromdb).isEqualTo(savedJobPosting);
		assertThat(getCompany).isEqualTo(savedCompany);

	}

	@Test
	void testGetJobsByCompany() {
		Company company = new Company("FST", "Tunis", "fst@gmail.com", "3654");
		JobPosting jobPosting1 = new JobPosting(company, 1, "Teacher", "teaching", "no prerequis", "sfax", null, null);

		JobPosting jobPosting2 = new JobPosting(company, 2, "English professor", "teaching", "no prerequis", "tunis",
				null, null);
		List<JobPosting> listJobs = Arrays.asList(jobPosting1, jobPosting2);
		company.setCompanyJobs(listJobs);
		Company savedCompany = companyDao.save(company);
		System.out.println(savedCompany.getCompanyId());
		Company getCompany = companyDao.findById(1).get();
		// companyService.getCompanyById(savedCompany.getCompanyId());
		// List<JobPosting> companyJobsList = jobPostingDao.findByCompany(savedCompany);
		List<JobPosting> companyJobsList = jobPostingDao.findByCompany(getCompany);
		assertThat(companyJobsList).isNotEmpty();
	}

}
