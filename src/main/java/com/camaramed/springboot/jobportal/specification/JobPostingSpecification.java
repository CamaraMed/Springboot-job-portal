package com.camaramed.springboot.jobportal.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.camaramed.springboot.jobportal.entity.JobPosting;

@Component
public class JobPostingSpecification {

	public static Specification<JobPosting> likeKewords(String keywords) {
		if (keywords == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("keywords"), "%" + keywords + "%");
		};
	}

	public static Specification<JobPosting> likeTitle(String title) {
		if (title == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("title"), "%" + title + "%");
		};
	}

	public static Specification<JobPosting> equalLocation(String location) {
		if (location == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.equal(root.get("location"), location);
		};
	}

	/*
	 * public static Specification<JobPosting> likeCompany(String company) { if
	 * (company == null) { return null; } return (root, query, cb) -> { return
	 * cb.like(root.get("company"), "%" + company + "%"); }; }
	 */

}
