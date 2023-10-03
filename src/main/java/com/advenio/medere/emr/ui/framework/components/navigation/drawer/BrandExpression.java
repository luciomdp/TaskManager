package com.advenio.medere.emr.ui.framework.components.navigation.drawer;

import com.advenio.medere.emr.ui.UIUtils;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/components/brand-expression.css")
public class BrandExpression extends Div {

	private static final long serialVersionUID = 6789806507758515900L;

	private String CLASS_NAME = "brand-expression";

    private Image logo;
    private Label title;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
    	super.onAttach(attachEvent);
    	UIUtils.getLogoImage(logo);
        logo.setWidth(150, Unit.PIXELS);
		logo.setHeight(150, Unit.PIXELS);
    }
    
    public BrandExpression(String text) {
        setClassName(CLASS_NAME);

        logo = new Image();
        logo.setAlt("logo");
        logo.setAlt(text + " logo");
        logo.setClassName(CLASS_NAME + "__logo");

        title = UIUtils.createH4Label(text);
        title.addClassName(CLASS_NAME + "__title");

        add(logo, title);
    }

}
