package com.camaramed.springboot.jobportal.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camaramed.springboot.jobportal.entity.Company;
import com.camaramed.springboot.jobportal.entity.JobApplication;
import com.camaramed.springboot.jobportal.entity.JobPosting;
import com.camaramed.springboot.jobportal.entity.JobSeeker;
import com.camaramed.springboot.jobportal.service.InterestedService;
import com.camaramed.springboot.jobportal.service.JobApplicationService;
import com.camaramed.springboot.jobportal.service.JobPostingService;
import com.camaramed.springboot.jobportal.service.JobSeekerService;
import com.camaramed.springboot.jobportal.service.Impl.EmailServiceImpl;

@Controller
//@RestController
@RequestMapping(value = "/application")
public class JobApplicationController {

	@Autowired
	JobSeekerService jobSeekerService;

	@Autowired
	EmailServiceImpl emailService;

	@Autowired
	JobPostingService jobDao;

	@Autowired
	JobApplicationService jobAppService;

	@Autowired
	InterestedService interestedService;

	@Autowired
	private EntityManager entityManager;

	private static String UPLOADED_FOLDER = "C:/";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String applyPage(@RequestParam("userId") String jobSeekerId, @RequestParam("jobId") String jobId,
			Model model) {

		return "jobapplication";
	}

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(@RequestParam("userId") String jobSeekerId, @RequestParam("jobId") String jobId,
			@RequestParam("resumeFlag") boolean resumeFlag, @RequestParam("resumePath") String resumePath,
			@RequestParam("file") Optional<MultipartFile> file, RedirectAttributes redirectAttributes, Model model) {
		if (resumeFlag == true) {

			System.out.println("In Job Contraoller");
			if (file.equals(Optional.empty())) {
				System.out.println("Inside Empty");
				redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
				return "redirect:uploadStatus";
			}

			try {
				System.out.println("Inside Upload");
				byte[] bytes = file.get().getBytes();
				Path path = Paths.get(UPLOADED_FOLDER + file.get().getOriginalFilename());
				JobApplication ja = new JobApplication();
				ja = jobAppService.apply(Integer.parseInt(jobSeekerId), Integer.parseInt(jobId), resumeFlag,
						path.toString());// apply(Integer.parseInt(jobSeekerId),
											// Integer.parseInt(jobId),
											// resumeFlag, path);
				JobSeeker js = jobSeekerService.getJobSeeker(Integer.parseInt(jobSeekerId));
				JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
				emailService.sendSimpleMessage(js.getEmailId(),
						"You have successfully applied to the position " + jp.getTitle() + " at "
								+ jp.getCompany().getCompanyName(),
						"Hi " + js.getFirstName() + " " + js.getLastName()
								+ ".\n You have successfully completed your application for " + jp.getTitle() + " at "
								+ jp.getCompany().getCompanyName() + ".\n Regards,\nThe FindJobs Team");

				Company company = jp.getCompany();
				List<?> ij = interestedService.getAllInterestedJobId(Integer.parseInt(jobSeekerId));
				int i = 0;
				int j = 0;
				if (ij.contains(Integer.parseInt(jobId))) {
					i = 1;
				}

				List<Integer> il = getAppliedJobs(jobSeekerId);
				if (il.contains(Integer.parseInt(jobId))) {
					j = 1;
				}

				model.addAttribute("job", jp);
				model.addAttribute("seeker", js);
				model.addAttribute("company", company);
				model.addAttribute("interested", i);
				model.addAttribute("applied", j);
				System.out.println(path);
				Files.write(path, bytes);
				System.out.println(path);
				return "userjobprofile";

				/// redirectAttributes.addFlashAttribute("message",
				// "You successfully uploaded '" +
				/// file.get().getOriginalFilename() + "'");

			} catch (IOException e) {
				e.printStackTrace();
			}

			return "redirect:/userjobprofile";

		} else {
			JobApplication ja = new JobApplication();
			ja = jobAppService.apply(Integer.parseInt(jobSeekerId), Integer.parseInt(jobId), resumeFlag, resumePath);
			JobSeeker js = jobSeekerService.getJobSeeker(Integer.parseInt(jobSeekerId));
			JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
			emailService.sendSimpleMessage(js.getEmailId(),
					"You have successfully applied to the position " + jp.getTitle() + " at "
							+ jp.getCompany().getCompanyName(),
					"Hi " + js.getFirstName() + " " + js.getLastName()
							+ ".\n You have successfully completed your application for " + jp.getTitle() + " at "
							+ jp.getCompany().getCompanyName() + ".\n Regards,\nThe FindJobs Team");
			Company company = jp.getCompany();
			List<?> ij = interestedService.getAllInterestedJobId(Integer.parseInt(jobSeekerId));
			int i = 0, j = 0;
			if (ij.contains(Integer.parseInt(jobId))) {
				i = 1;
			}

			List<Integer> il = getAppliedJobs(jobSeekerId);
			if (il.contains(Integer.parseInt(jobId))) {
				j = 1;
			}

			model.addAttribute("job", jp);
			model.addAttribute("seeker", js);
			model.addAttribute("company", company);
			model.addAttribute("interested", i);
			model.addAttribute("applied", j);

			return "userjobprofile";

		}

	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String cancelApplication(@RequestParam("jobApplicationId") String jobAppId) {
		boolean deleted = jobAppService.cancel(Integer.parseInt(jobAppId));
		if (deleted)
			return "Cancelled";
		return "Unable to delete";
	}

	@RequestMapping(value = "/modifyapplicationstate", method = RequestMethod.POST)
	public String modifyApplicationState(@RequestParam("jobAppId") String jobAppId,
			@RequestParam("state") String state) {
		JobApplication ja = jobAppService.modifyJobApplicationStatus(Integer.parseInt(jobAppId),
				Integer.parseInt(state));
		if (ja == null) {
			return "Error";
		}
		return "modified";
	}

	// ***************************************************
	@RequestMapping("/viewResume")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("Id") int id) throws IOException {
		JobApplication j1 = jobAppService.getJobApplication(id);
		String path = j1.getResumePath();

		File file = new File(path);
		System.out.println(file);

		if (file.exists()) {
			System.out.println("File Found");
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			// Copy bytes from source to destination(outputstream in this
			// example), closes both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}

	}

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			System.out.println(path);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

	@RequestMapping(value = "/company/getAppliedJobs", method = RequestMethod.GET)
	public ResponseEntity<?> getAppliedJobs(@RequestParam("companyId") int id) {
		Query query = entityManager.createQuery("SELECT jobId FROM JobPosting jp WHERE jp.companyId = :id");
		query.setParameter("id", id);
		List<Integer> list = new ArrayList<Integer>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			int uid = (int) iterator.next();
			list.add(uid);
			System.out.println(uid);
		}

		return ResponseEntity.ok("data");
	}

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
