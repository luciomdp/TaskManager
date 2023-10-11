package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    
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


    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profile;
    private String description;

}
