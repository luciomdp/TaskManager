package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Area {
    
	public enum Areas {
		INFORMATIC(1);

		private long value;

		private Areas(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}


    @Id
    private Long area;
    private String description;

}
