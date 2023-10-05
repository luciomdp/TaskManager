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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long area;
    private String description;

}
