package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
	@Id
	private Long userid;
	private String username;
	private String name;
	@ManyToOne
	@JoinColumn(name = "profile")
	private Profile profile;

	
}
