package com.camaramed.springboot.jobportal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author amayd
 *
 */
@Entity
@Table(name = "jobposting")
public class JobPosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "job_id", unique = true, nullable = false)
	private int jobId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "state")
	private int state;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "responsibilities")
	private String responsibilities;

	@Column(name = "location")
	private String location;

	@Column(name = "salary")
	private String salary;

	@Column(name = "keywords")
	private String keywords;

	// @OneToMany(mappedBy="passenger", cascade = CascadeType.ALL)
	// @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,
	// property="orderId")
	// private List<Reservation> reservations;

	public JobPosting() {
	}

	public JobPosting(Company company, int state, String title, String description, String responsibilties,
			String location, String salary, String keywords) {
		super();
		this.company = company;
		this.state = state;
		this.title = title;
		this.description = description;
		this.responsibilities = responsibilties;
		this.location = location;
		this.salary = salary;
		this.keywords = keywords;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilties) {
		this.responsibilities = responsibilties;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}