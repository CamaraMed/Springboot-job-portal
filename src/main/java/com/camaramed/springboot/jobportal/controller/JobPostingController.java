package com.camaramed.springboot.jobportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.service.CompanyService;
import com.camaramed.springboot.jobportal.service.JobPostingService;

//@RestController
@Controller
@RequestMapping("/JobPosting")
public class JobPostingController {

	@Autowired
	JobPostingService jobService;

	@Autowired
	CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET)
	public String showHomePage(@RequestParam("cid") String cid, Model model) {
		System.out.println(cid);

		Company company = companyService.getCompanyById(Integer.parseInt(cid));
		model.addAttribute("cid", cid);
		model.addAttribute("company", company);
		return "postjob";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createJobPosting(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("cid") String cid, Model model) {
		JobPosting j = new JobPosting();
		Company company = companyService.getCompanyById(Integer.parseInt(cid));
		j.setTitle(title);
		j.setDescription(description);
		j.setResponsibilities(responsibilities);
		j.setLocation(location);
		j.setSalary(salary);
		j.setKeywords(title + " " + description + " " + responsibilities + " " + location);
		j.setCompany(company);

		try {
			System.out.println("0");

			JobPosting p1 = jobService.createJobPosting(j);
			System.out.println("ashay");

			model.addAttribute("job", p1);
			// Company company = companyDao.getCompany(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";

		} catch (Exception e) {
			HttpHeaders httpHeaders = new HttpHeaders();
			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "another passenger with the phone number  already exists.");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";
		}

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteJobPosting(@PathVariable("id") int id, Model model) {

		if (jobService.deleteJobPosting(id)) {
			String message = "Job Posting with JobID " + id + " is deleted successfully";
			model.addAttribute("message", message);
			return "message";
		} else {
			return "error";
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String showUpdatePage(@PathVariable("id") int id, @RequestParam("cid") String cid, Model model) {
		System.out.println(cid);
		System.out.println(id);

		Company company = companyService.getCompanyById(Integer.parseInt(cid));
		JobPosting jp = jobService.getJobPosting(id);
		model.addAttribute("job", jp);
		model.addAttribute("company", company);
		return "updatejob";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String updateJobPosting(@PathVariable("id") int id, @RequestParam("state") String state,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("cid") String cid, Model model) {
		// TODO routing
		JobPosting job = jobService.getJobPosting(id);
		if (job != null) {
			job.setJobId(id);
			job.setDescription(description);
			job.setState(Integer.parseInt(state));
			job.setTitle(title);
			job.setLocation(location);
			job.setResponsibilities(responsibilities);
			JobPosting p1 = jobService.updateJobPosting(job);

			model.addAttribute("job", p1);
			Company company = companyService.getCompanyById(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";
		}
		return "error";

	}

	@RequestMapping(value = "/modifyjobstate", method = RequestMethod.POST)
	public String modifyJobState(@RequestParam("jobId") String jobId, @RequestParam("state") String state) {
		JobPosting jp = jobService.getJobPosting(Integer.parseInt(jobId));
		jp.setState(Integer.parseInt(state));
		jp = jobService.updateJobPosting(jp);
		if (jp == null) {
			return "Error";
		}
		return "modified";
	}

}
