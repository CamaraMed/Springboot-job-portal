package com.camaramed.springboot.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.camaramed.springboot.jobportal.dao.InterestedDao;
import com.camaramed.springboot.jobportal.dao.JobSeekerDao;
import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.service.CompanyService;
import com.camaramed.springboot.jobportal.service.JobPostingService;

@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private JobPostingService jobPostingService;

	@Autowired
	InterestedDao interestedDao;

	@Autowired
	JobSeekerDao jobSeekerDao;

	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
	public String showJobSeeker(@PathVariable("id") Integer id, Model model) {

		Company company = companyService.getCompanyById(id);

		model.addAttribute("company", company);
		return "companyprofile";
	}

	@RequestMapping(value = "/showjob", method = RequestMethod.GET)
	public String showJob(@RequestParam("cid") String cid, @RequestParam("jobId") String jobId, Model model) {

		JobPosting p1 = jobPostingService.getJobPosting(Integer.parseInt(jobId));
		Company company = companyService.getCompanyById(Integer.parseInt(cid));
		model.addAttribute("job", p1);
		model.addAttribute("company", company);
		return "jobprofile";
	}

	@RequestMapping(value = "/showapplicants", method = RequestMethod.GET)
	public String showJobApplicants(@RequestParam("jobId") String jobId, Model model) {

		JobPosting p1 = jobPostingService.getJobPosting(Integer.parseInt(jobId));
		model.addAttribute("job", p1);

		return "jobprofile";

	}

	@RequestMapping(value = "/getjobs", method = RequestMethod.GET)
	public String getJobs(@RequestParam("companyId") String companyId, Model model) {
		List<JobPosting> companyJobPostings = new ArrayList<>();
		Company company = companyService.getCompanyById(Integer.parseInt(companyId));// Integer.parseInt(companyId)
		companyJobPostings = companyService.getJobsByCompany(company);// Integer.parseInt(companyId)

		model.addAttribute("jobs", companyJobPostings);
		model.addAttribute("company", company);

		return "companyjobs";
	}

}
