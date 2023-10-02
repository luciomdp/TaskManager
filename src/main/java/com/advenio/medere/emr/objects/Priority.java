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
public class Priority {

    public enum Priorities {
        HIGH(1),
        MEDIUM(2),
        LOW(3);

		private long value;

		private Priorities(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}


    @Id
    private Long priority;
    private String description;
}
