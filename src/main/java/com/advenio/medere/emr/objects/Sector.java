package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sector {

    public enum Sectors {
		DEVELOPMENT(1), COMUNICATION(2), SUPPORT(3);

		private long value;

		private Sectors(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sector;
    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "sectormanager")
    private User sector_manager;
    @ManyToOne
    @JoinColumn(name = "area")
    private Area area;
}
