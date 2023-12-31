package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    public enum Categories {
        DEVELOPMENT_REQUEST(1),
        MODIFICATION_REQUEST(2),
        ERROR_CORRECTION(3),
        INTERNET_PROBLEMS(4),
        INTERNAL_NETWORK_PROBLEMS(5),
        SERVICE_ISSUES(6),
        EMAIL_SERVICE(7),
        DATABASE_SERVICE(8),
        OPERATING_SYSTEM_HELP(9),
        HARDWARE_OPERATION_HELP(10);

		private long value;

		private Categories(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category;
    @ManyToOne
    @JoinColumn(name = "sector")
    private Sector sector;
    private String description;
}
