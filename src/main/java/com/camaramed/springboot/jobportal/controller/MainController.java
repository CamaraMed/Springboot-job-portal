package com.camaramed.springboot.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.camaramed.springboot.jobportal.dao.CompanyDao;
import com.camaramed.springboot.jobportal.dao.JobSeekerDao;
import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobSeeker;
import com.camaramed.springboot.jobportal.service.CompanyService;
import com.camaramed.springboot.jobportal.service.JobSeekerService;
import com.camaramed.springboot.jobportal.service.Impl.EmailServiceImpl;

@Controller
//@RestController
@RequestMapping(value = "/")
public class MainController {

	@Autowired
	JobSeekerDao jobSeekerDao;

	@Autowired
	JobSeekerService jobSeekerService;

	@Autowired
	CompanyDao companyDao;

	@Autowired
	EmailServiceImpl emailService;

	@Autowired
	CompanyService companyService;

	@GetMapping("/home")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/findjobs", method = RequestMethod.GET)
	public String showHomePage() {
		return "index";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegisterPage() {
		return "register";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("emailId") String emailId, @RequestParam("password") String password,
			@RequestParam("type") String type, Model model) {
		List<String> list = new ArrayList<String>();
		String email = emailId;
		String pwd = password;
		System.out.println(email + " : " + pwd);
		String message = "<div class=\"alert alert-danger\">Invalid Login Credentials</div>";

		if (type.equals("recruiter")) {
			list = companyService.PasswordLookUp(email);
			if (list.size() == 0) {

				model.addAttribute("message", message);
				return "index";
			} else {
				if (pwd.equals(list.get(0))) {
					List<Integer> cidl = new ArrayList<Integer>();
					cidl = companyService.getCompanyIdFromEmail(email);
					Company cmp = companyService.getCompanyById(cidl.get(0));
					model.addAttribute("company", cmp);

					return "companyprofile";
				}

			}

		} else if (type.equals("seeker")) {
			list = jobSeekerService.PasswordLookUp(email);
			if (list.size() == 0) {
				model.addAttribute("message", message);

				return "index";
			} else {
				if (pwd.equals(list.get(0))) {
					List<Integer> jsl = new ArrayList<Integer>();
					jsl = jobSeekerService.getUserIdFromEmail(email);
					JobSeeker js = jobSeekerService.getJobSeeker(jsl.get(0));

					model.addAttribute("seeker", js);
					return "userprofile";
				}

			}
		}

		System.out.println(list);
		model.addAttribute("message", message);

		return "index";

	}

	@RequestMapping(value = "/register/verify", method = RequestMethod.GET)
	public String verification(@RequestParam("type") String type, @RequestParam("pin") int pin,
			@RequestParam("userId") int userId, Model model) {

		if (type.equals("seeker")) {

			JobSeeker j = jobSeekerService.getJobSeeker(userId);
			if (j.getVerificationCode() == pin) {
				j.setVerified(true);
				jobSeekerService.verify(j);
				model.addAttribute("seeker", j);
				return "userregister";
			} else {
				return "error";

			}

		} else {

			Company j = companyService.getCompanyById(userId);
			if (j.getVerificationCode() == pin) {
				j.setVerified(true);
				companyService.verify(j);
				model.addAttribute("company", j);
				return "companyregister";
			} else {
				return "error";
			}

		}

	}

}