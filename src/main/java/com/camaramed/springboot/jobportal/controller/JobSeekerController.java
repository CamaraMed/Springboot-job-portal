package com.camaramed.springboot.jobportal.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.camaramed.springboot.jobportal.dao.JobPostingDao;
import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.Interested;
import com.camaramed.springboot.jobportal.entity.JobApplication;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.entity.JobSeeker;
import com.camaramed.springboot.jobportal.service.CompanyService;
import com.camaramed.springboot.jobportal.service.InterestedService;
import com.camaramed.springboot.jobportal.service.JobPostingService;
import com.camaramed.springboot.jobportal.service.JobSeekerService;
import com.camaramed.springboot.jobportal.specification.JobPostingSpecification;

@Controller

@RequestMapping(value = "/")
public class JobSeekerController {

	@Autowired
	private JobSeekerService jobSeekerService;

	@Autowired
	private JobPostingDao jobPostingDao;

	@Autowired
	private CompanyService companyService;

	@Autowired
	JobPostingService jobPostingService;

	@Autowired
	InterestedService interestedService;

	// @Autowired
	// EmailServiceImpl emailService;

	// @Autowired
	// InterestedDao interestedDao;

	// @PersistenceContext
	// private EntityManager entityManager;

	@RequestMapping(value = "/searchjobs", method = RequestMethod.GET)
	public String searchJobs(@RequestParam("userId") String userId,

			@RequestParam("searchString") Optional<String> searchString,

			@RequestParam("locations") Optional<String> locations,
			// @RequestParam("companies") Optional<String> companies,
			// @RequestParam("min") Optional<String> min, // @RequestParam("max")
			// Optional<String> max,
			Model model) {
		String title = ""; // String company ="";
		String location = "";
		// String keword = "";

		if (!searchString.equals(Optional.empty())) {
			title = searchString.get();

		}

		if (!locations.equals(Optional.empty())) {

			System.out.println("location");
			location = locations.get();
		}

		Specification<JobPosting> specification = Specification
				.where(JobPostingSpecification.likeTitle(title).and(JobPostingSpecification.equalLocation(location)));

		List<JobPosting> jobs = jobPostingDao.findAll(specification);
		JobSeeker jobseeker = jobSeekerService.getJobSeeker(Integer.parseInt(userId));
		model.addAttribute("jobs", jobs);
		model.addAttribute("seeker", jobseeker);

		return "jobsearch";
	}

	@RequestMapping(value = "/showjob", method = RequestMethod.GET)
	public String showJob(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId, Model model) {

		JobPosting job = jobPostingService.getJobPosting(Integer.parseInt(jobId));
		Company company = job.getCompany();
		JobSeeker seeker = jobSeekerService.getJobSeeker(Integer.parseInt(userId));
		List<?> ij = interestedService.getAllInterestedJobId(Integer.parseInt(userId));
		int i = 0, j = 0;
		if (ij.contains(Integer.parseInt(jobId))) {
			i = 1;
		}

		List<Integer> il = getAppliedJobs(userId);
		if (il.contains(Integer.parseInt(jobId))) {
			j = 1;
		}

		model.addAttribute("job", job);
		model.addAttribute("seeker", seeker);
		model.addAttribute("company", company);
		model.addAttribute("interested", i);
		model.addAttribute("applied", j);

		return "userjobprofile";
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createJobSeeker(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("type") String type, Model model)
			throws IOException, SQLException {

		int randomPIN = (int) (Math.random() * 9000) + 1000;
		String[] splited = name.split("\\s+");

		try {

			if (type.equals("seeker")) {

				JobSeeker j = new JobSeeker();
				j.setFirstName(splited[0]);
				j.setLastName(splited[1]);
				j.setPassword(password);
				j.setEmailId(email);
				j.setVerificationCode(randomPIN);
				j.setVerified(false);

				JobSeeker j1 = jobSeekerService.createJobSeeker(j);
				model.addAttribute("name", j1.getFirstName());
				return "codesent";

			}

			else {

				Company c = new Company();
				c.setVerified(false);
				c.setVerificationCode(randomPIN);
				c.setCompanyName(name);
				c.setCompanyUser(email);
				c.setPassword(password);
				c.setHeadquarters("head");

				Company c1 = companyService.createCompany(c);
				model.addAttribute("name", c1.getCompanyName());
				// Company c1 =companyDao.
				return "codesent";
			}

		} catch (SQLException se) {
			HttpHeaders httpHeaders = new HttpHeaders();
			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Email Already Exists");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		} catch (Exception se) {
			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String updateSeekerPage(@RequestParam("id") String id, Model model) {

		JobSeeker j1 = jobSeekerService.getJobSeeker(Integer.parseInt(id));
		model.addAttribute("j", j1);

		return "updateSeeker";
	}

	@RequestMapping(value = "/userprofile/{id}", method = RequestMethod.GET)
	public String showJobSeeker(@PathVariable("id") int id, Model model) {

		JobSeeker jobseeker = jobSeekerService.getJobSeeker(id);

		model.addAttribute("seeker", jobseeker);
		return "userprofile";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateJobSeeker(@RequestParam("id") String id, @RequestParam("firstname") Optional<String> firstname,
			@RequestParam("lastname") Optional<String> lastname, @RequestParam("emailid") Optional<String> emailid,
			@RequestParam("highesteducation") Optional<String> highesteducation,
			@RequestParam("password") Optional<String> password, @RequestParam("skills") Optional<String> skills,
			@RequestParam("workex") Optional<String> workex, Model model) throws Exception {
		JobSeeker js = new JobSeeker();

		js.setJobseekerId(Integer.parseInt(id));

		if (!emailid.equals(Optional.empty())) {
			System.out.println("emailid done : " + emailid.get() + ":::: " + emailid);
			js.setEmailId(emailid.get());
		}
		if (!firstname.equals(Optional.empty())) {
			System.out.println("fname done");
			js.setFirstName(firstname.get());
		}
		if (!lastname.equals(Optional.empty())) {
			System.out.println("lname done");
			js.setLastName(lastname.get());
		}
		if (!highesteducation.equals(Optional.empty())) {
			System.out.println("highest edu");
			js.setHighestEducation(Integer.parseInt(highesteducation.get()));
		}
		if (!password.equals(Optional.empty())) {
			System.out.println("password");
			js.setPassword(password.get());
		}
		if (!skills.equals(Optional.empty())) {
			System.out.println("skills : " + skills);
			js.setSkills(skills.get());
			System.out.println("skills done");
		}

		if (!workex.equals(Optional.empty())) {
			System.out.println("workex : " + workex);
			js.setWorkEx(Integer.parseInt(workex.get()));
		}

		JobSeeker jobseeker = jobSeekerService.getJobSeeker(Integer.parseInt(id));
		JobSeeker jobskr = null;
		if (jobseeker != null) {
			jobskr = jobSeekerService.updateJobSeeker(js);
			System.out.println("updated");
		} else {
			jobskr = jobSeekerService.createJobSeeker(js);
		}
		System.out.println("done");
		System.out.println("ashay");
		System.out.println(jobskr.getVerificationCode());

		model.addAttribute("seeker", jobskr);
		return "userprofile";

	}

	@RequestMapping(value = "/update/company", method = RequestMethod.POST)
	public String companyupdate(@RequestParam("id") String id, @RequestParam("companyName") Optional<String> name,
			@RequestParam("headquarters") Optional<String> headquarters,
			@RequestParam("companyUser") Optional<String> user,
			@RequestParam("description") Optional<String> description, Model model) {

		Company c = new Company();

		c.setCompanyId(Integer.parseInt(id));

		if (!name.equals(Optional.empty())) {

			c.setCompanyName(name.get());
		}
		if (!user.equals(Optional.empty())) {

			c.setCompanyUser(user.get());
		}
		if (!headquarters.equals(Optional.empty())) {
			c.setHeadquarters(headquarters.get());
		}
		if (!description.equals(Optional.empty())) {
			c.setDescription(description.get());
		}

		Company company = companyService.getCompanyById(Integer.parseInt(id));
		Company c1 = null;
		if (company != null) {
			c1 = companyService.updateCompany(c);

		} else {
			return "error";
		}
		System.out.println("done");
		model.addAttribute("company", c1);
		return "companyprofile";

	}

	@RequestMapping(value = "/interested", method = RequestMethod.POST)
	public String createInterest(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId,
			Model model) {

		try {
			Interested in = new Interested();
			in.setJobId(Integer.parseInt(jobId));
			in.setJobSeekerId(Integer.parseInt(userId));
			Interested i1 = interestedService.createInterest(in);

		} catch (Exception e) {

			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		}
		JobPosting job = jobPostingService.getJobPosting(Integer.parseInt(jobId));
		Company company = job.getCompany();
		JobSeeker seeker = jobSeekerService.getJobSeeker(Integer.parseInt(userId));
		List<?> ij = interestedService.getAllInterestedJobId(Integer.parseInt(userId));
		int i = 0, j = 0;
		if (ij.contains(Integer.parseInt(jobId))) {
			i = 1;
		}
		String message = "<div class=\"alert alert-success\">This job has been <strong>Successfully added</strong> to your interests</div>";

		List<Integer> il = getAppliedJobs(userId);
		if (il.contains(Integer.parseInt(jobId))) {
			j = 1;
		}

		model.addAttribute("job", job);
		model.addAttribute("seeker", seeker);
		model.addAttribute("company", company);
		model.addAttribute("interested", i);
		model.addAttribute("message", message);
		model.addAttribute("applied", j);

		return "userjobprofile";
	}

	@RequestMapping(value = "/interested/delete", method = RequestMethod.POST)
	public String deleteInterest(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId,
			Model model) {

		try {
			List<?> querylist = interestedService.getInterestedJobId(Integer.parseInt(jobId), Integer.parseInt(userId));
			boolean interestDeleted = interestedService.deleteInterest(Integer.parseInt(querylist.get(0).toString()));
			if (interestDeleted) {
				JobPosting job = jobPostingService.getJobPosting(Integer.parseInt(jobId));
				Company company = job.getCompany();
				JobSeeker seeker = jobSeekerService.getJobSeeker(Integer.parseInt(userId));
				List<?> ij = interestedService.getAllInterestedJobId(Integer.parseInt(userId));
				int i = 0;
				if (ij.contains(Integer.parseInt(jobId))) {
					i = 1;
				}

				String message = "<div class=\"alert alert-danger\">This job has been <strong>Successfully removed</strong> from your interests</div>";

				model.addAttribute("job", job);
				model.addAttribute("seeker", seeker);
				model.addAttribute("company", company);
				model.addAttribute("interested", i);
				model.addAttribute("message", message);
				model.addAttribute("applied", 1);

				return "userjobprofile";

			} else {
				return "error";
			}

		} catch (Exception e) {

			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		}

	}

	@RequestMapping(value = "/getinterestedjobs", method = RequestMethod.GET)
	public String getInterestedJobsForJobSeeker(@RequestParam("jobSeekerId") String jobSeekerId, Model model) {

		JobSeeker jobseeker = jobSeekerService.getJobSeeker(Integer.parseInt(jobSeekerId));
		List<?> jobSeekerInterestsList = jobSeekerService.getJobSeeker(Integer.parseInt(jobSeekerId))
				.getInterestedjobs();

		model.addAttribute("jobs", jobSeekerInterestsList);
		model.addAttribute("seeker", jobseeker);
		return "interestedjobs";
	}

	@RequestMapping(value = "/getappliedjobs", method = RequestMethod.GET)
	public List<Integer> getAppliedJobs(@RequestParam("jobSeekerId") String jobSeekerId) {
		List<?> jobSeekerAppliedList = jobSeekerService.getJobSeeker(Integer.parseInt(jobSeekerId))
				.getJobApplicationList();
		List<Integer> jobIdList = new ArrayList<Integer>();
		for (Iterator iterator = jobSeekerAppliedList.iterator(); iterator.hasNext();) {
			JobApplication ja = (JobApplication) iterator.next();
			int jobId = ja.getJobPosting().getJobId();
			jobIdList.add(jobId);
		}
		return jobIdList;
	}

}