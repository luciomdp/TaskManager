package com.advenio.medere.emr.objects.user;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

	private Long userid;
	private String username;
	private String name;

	public enum Profiles {
		USER(1), SPECIALIST (2), SECTOR_MANAGER(3), AREA_MANAGER(4);

		private long value;

		private Profiles(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}
	
}
