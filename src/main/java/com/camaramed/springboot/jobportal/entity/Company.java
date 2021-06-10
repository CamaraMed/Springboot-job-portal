package com.camaramed.springboot.jobportal.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "company_id", unique = true, nullable = false)
	private Integer companyId;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "headquarters")
	private String headquarters;

	@Column(name = "company_user")
	private String companyUser;

	@Column(name = "password")
	private String password;

	@Column(name = "description")
	private String description;

	@Column(name = "verified")
	private boolean verified;
	@Column(name = "verification_code")
	private int verificationCode;

	@OneToMany(targetEntity = JobPosting.class, mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<JobPosting> CompanyJobs = new ArrayList<>();

	public Company() {
	}

	public Company(String companyName, String headquarters, String companyUser, String password) {
		super();
		this.companyName = companyName;
		this.headquarters = headquarters;
		this.companyUser = companyUser;
		this.password = password;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getHeadquarters() {
		return headquarters;
	}

	public void setHeadquarters(String headquarters) {
		this.headquarters = headquarters;
	}

	public String getCompanyUser() {
		return companyUser;
	}

	public void setCompanyUser(String companyUser) {
		this.companyUser = companyUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(int verificationCode) {
		this.verificationCode = verificationCode;
	}

	public List<JobPosting> getCompanyJobs() {
		return CompanyJobs;
	}

	public void setCompanyJobs(List<JobPosting> companyJobs) {
		CompanyJobs = companyJobs;
	}

}
