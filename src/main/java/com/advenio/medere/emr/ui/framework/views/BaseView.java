package com.advenio.medere.emr.ui.framework.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.advenio.medere.MessageBusContainer;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;

public abstract class BaseView extends ViewFrame{

	@Autowired
	protected MessageBusContainer eventBus;
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		eventBus.register(this);
	}
	
	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
	}
}
