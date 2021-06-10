package com.camaramed.springboot.jobportal.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author amayd
 *
 */
@Entity
@Table(name = "jobseeker")
public class JobSeeker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "job_seeker_id", unique = true, nullable = false)
	private int jobseekerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "password")
	private String password;

	@Column(name = "work_ex")
	private int workEx;

	@Column(name = "highest_education")
	private int highestEducation;

	@Column(name = "skills")
	private String skills;

	@Column(name = "verified")
	private boolean verified;

	@Column(name = "verification_code")
	private int verificationCode;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "interested", joinColumns = @JoinColumn(name = "job_seeker_id", referencedColumnName = "job_seeker_id"), inverseJoinColumns = @JoinColumn(name = "job_id"))
	private List<JobPosting> interestedjobs;

	@OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "appId")
	private List<JobApplication> jobApplicationList;

	/**
	 * @return JobSeeker Id
	 */
	public int getJobseekerId() {
		return jobseekerId;
	}

	/**
	 * @param jobseekerId
	 */
	public void setJobseekerId(int jobseekerId) {
		this.jobseekerId = jobseekerId;
	}

	/**
	 * @return First Name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Last Name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return EmailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return Password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Work Experience In Years
	 */
	public int getWorkEx() {
		return workEx;
	}

	/**
	 * @param workEx
	 */
	public void setWorkEx(int workEx) {
		this.workEx = workEx;
	}

	/**
	 * @return Highest Education
	 */
	public int getHighestEducation() {
		return highestEducation;
	}

	/**
	 * @param highestEducation
	 */
	public void setHighestEducation(int highestEducation) {
		this.highestEducation = highestEducation;
	}

	/**
	 * @return Skills
	 */
	public String getSkills() {
		return skills;
	}

	/**
	 * @param skills
	 */
	public void setSkills(String skills) {
		this.skills = skills;
	}

	/**
	 * @return true if the user has been verified using the verification code
	 */
	public boolean isVerified() {
		return verified;
	}

	/**
	 * @param verified
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	/**
	 * @return verificationCode
	 */
	public int getVerificationCode() {
		return verificationCode;
	}

	/**
	 * @param verificationCode
	 */
	public void setVerificationCode(int verificationCode) {
		this.verificationCode = verificationCode;
	}

	/**
	 * @return List of jobs the job seeker is interested in
	 */
	public List<JobPosting> getInterestedjobs() {
		return interestedjobs;
	}

	/**
	 * @param interestedjobs
	 */
	public void setInterestedjobs(List<JobPosting> interestedjobs) {
		this.interestedjobs = interestedjobs;
	}

	public List<JobApplication> getJobApplicationList() {
		return jobApplicationList;
	}

	public void setJobApplicationList(List<JobApplication> jobApplicationList) {
		this.jobApplicationList = jobApplicationList;
	}

}
