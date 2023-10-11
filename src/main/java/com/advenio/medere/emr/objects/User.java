package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "_User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long _user;
	private String username;
	private String password;
	private String name;
	@ManyToOne
	@JoinColumn(name = "profile")
	private Profile profile;
	@ManyToOne
	@JoinColumn(name = "sector")
	private Sector sector;
	@ManyToOne
	@JoinColumn(name = "areamanager")
	private Area areamanager;
	@ManyToOne
	@JoinColumn(name = "sectormanager")
	private Sector sectormanager;
	@ManyToOne
	@JoinColumn(name = "sectorspecialist")
	private Sector sectorspecialist;
}
