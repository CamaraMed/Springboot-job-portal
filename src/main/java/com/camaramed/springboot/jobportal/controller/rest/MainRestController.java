package com.camaramed.springboot.jobportal.controller.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.service.CompanyService;

@RestController
public class MainRestController {

	@Autowired
	private CompanyService companyService;

	@GetMapping(value = "/get/{id}")
	public Company get(@PathVariable("id") Integer id) {

		return companyService.getCompanyById(id);
	}

	@GetMapping("/findAllCompanies")
	public Collection<Company> getAllCompanies() {
		return companyService.findAllCompanies();

	}

	@RequestMapping(value = "/getjobs", method = RequestMethod.GET)
	public String getJobs(@RequestParam("companyId") String companyId) {
		List<JobPosting> companyJobPostings = new ArrayList<>();
		Company company = companyService.getCompanyById(Integer.parseInt(companyId));// Integer.parseInt(companyId)
		companyJobPostings = companyService.getJobsByCompany(company);// Integer.parseInt(companyId)

		return "companyJobPostings";
	}

}
