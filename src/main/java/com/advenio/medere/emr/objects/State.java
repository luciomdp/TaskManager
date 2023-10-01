package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class State {
   	public enum States {
		SIN_ASIGNAR(1), POR_REALIZAR (2), EN_PROCESO(3), FINALIZADO(4), CANCELADO(5);

		private long value;

		private States(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}

    public State (){

    }

    @Id
    private Long state;
    private String description; 
}
