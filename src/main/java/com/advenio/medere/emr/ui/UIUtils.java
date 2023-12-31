package com.advenio.medere.emr.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advenio.medere.ui.util.FontSize;
import com.advenio.medere.ui.util.FontWeight;
import com.advenio.medere.ui.util.IconSize;
import com.advenio.medere.ui.util.LineHeight;
import com.advenio.medere.ui.util.LumoStyles;
import com.advenio.medere.ui.util.TextColor;
import com.advenio.medere.ui.util.LumoStyles.FontFamily;
import com.advenio.medere.ui.util.LumoStyles.Heading;
import com.advenio.medere.ui.util.css.AlignSelf;
import com.advenio.medere.ui.util.css.BorderRadius;
import com.advenio.medere.ui.util.css.BoxSizing;
import com.advenio.medere.ui.util.css.Overflow;
import com.advenio.medere.ui.util.css.PointerEvents;
import com.advenio.medere.ui.util.css.Shadow;
import com.advenio.medere.ui.util.css.TextAlign;
import com.advenio.medere.ui.util.css.TextOverflow;
import com.advenio.medere.ui.util.css.WhiteSpace;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class UIUtils {

	public static final String IMG_PATH = "images/";
	private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);

	/**
	 * Thread-unsafe formatters.
	 */
	private static final ThreadLocal<DecimalFormat> decimalFormat = ThreadLocal
			.withInitial(() -> new DecimalFormat("###,###,###.00"));
	private static final ThreadLocal<DateTimeFormatter> dateFormat = ThreadLocal
			.withInitial(() -> DateTimeFormatter.ofPattern("MMM dd, YYYY"));

	/* ==== BUTTONS ==== */

	// Styles

	public static Button createPrimaryButton(String text) {
		return createButton(text, ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createPrimaryButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createPrimaryButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createTertiaryButton(String text) {
		return createButton(text, ButtonVariant.LUMO_TERTIARY);
	}

	public static Button createTertiaryButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_TERTIARY);
	}

	public static Button createTertiaryButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_TERTIARY);
	}

	public static Button createTertiaryInlineButton(String text) {
		return createButton(text, ButtonVariant.LUMO_TERTIARY_INLINE);
	}

	public static Button createTertiaryInlineButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_TERTIARY_INLINE);
	}

	public static Button createTertiaryInlineButton(String text,
			VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_TERTIARY_INLINE);
	}

	public static Button createSuccessButton(String text) {
		return createButton(text, ButtonVariant.LUMO_SUCCESS);
	}

	public static Button createSuccessButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_SUCCESS);
	}

	public static Button createSuccessButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_SUCCESS);
	}

	public static Button createSuccessPrimaryButton(String text) {
		return createButton(text, ButtonVariant.LUMO_SUCCESS,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createSuccessPrimaryButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_SUCCESS,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createSuccessPrimaryButton(String text,
			VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_SUCCESS,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createErrorButton(String text) {
		return createButton(text, ButtonVariant.LUMO_ERROR);
	}

	public static Button createErrorButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_ERROR);
	}

	public static Button createErrorButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_ERROR);
	}

	public static Button createErrorPrimaryButton(String text) {
		return createButton(text, ButtonVariant.LUMO_ERROR,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createErrorPrimaryButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_ERROR,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createErrorPrimaryButton(String text,
			VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_ERROR,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createContrastButton(String text) {
		return createButton(text, ButtonVariant.LUMO_CONTRAST);
	}

	public static Button createContrastButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_CONTRAST);
	}

	public static Button createContrastButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_CONTRAST);
	}

	public static Button createContrastPrimaryButton(String text) {
		return createButton(text, ButtonVariant.LUMO_CONTRAST,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createContrastPrimaryButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_CONTRAST,
				ButtonVariant.LUMO_PRIMARY);
	}

	public static Button createContrastPrimaryButton(String text,
			VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_CONTRAST,
				ButtonVariant.LUMO_PRIMARY);
	}

	// Size

	public static Button createSmallButton(String text) {
		return createButton(text, ButtonVariant.LUMO_SMALL);
	}

	public static Button createSmallButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_SMALL);
	}

	public static Button createSmallButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_SMALL);
	}

	public static Button createLargeButton(String text) {
		return createButton(text, ButtonVariant.LUMO_LARGE);
	}

	public static Button createLargeButton(VaadinIcon icon) {
		return createButton(icon, ButtonVariant.LUMO_LARGE);
	}

	public static Button createLargeButton(String text, VaadinIcon icon) {
		return createButton(text, icon, ButtonVariant.LUMO_LARGE);
	}

	// Text

	public static Button createButton(String text, ButtonVariant... variants) {
		Button button = new Button(text);
		button.addThemeVariants(variants);
		button.getElement().setAttribute("aria-label", text);
		return button;
	}

	// Icon

	public static Button createButton(VaadinIcon icon,
			ButtonVariant... variants) {
		Button button = new Button(new Icon(icon));
		button.addThemeVariants(variants);
		return button;
	}

	// Text and icon

	public static Button createButton(String text, VaadinIcon icon,
			ButtonVariant... variants) {
		Icon i = new Icon(icon);
		i.getElement().setAttribute("slot", "prefix");
		Button button = new Button(text, i);
		button.addThemeVariants(variants);
		return button;
	}

	/* ==== TEXTFIELDS ==== */

	public static TextField createSmallTextField() {
		TextField textField = new TextField();
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		return textField;
	}

	/* ==== LABELS ==== */

	public static Label createLabel(FontSize size, TextColor color,
			String text) {
		Label label = new Label(text);
		setFontSize(size, label);
		setTextColor(color, label);
		return label;
	}

	public static Label createLabel(FontSize size, String text) {
		return createLabel(size, TextColor.BODY, text);
	}

	public static Label createLabel(TextColor color, String text) {
		return createLabel(FontSize.M, color, text);
	}

	public static Label createH1Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H1);
		return label;
	}

	public static Label createH2Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H2);
		return label;
	}

	public static Label createH3Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H3);
		return label;
	}

	public static Label createH4Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H4);
		return label;
	}

	public static Label createH5Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H5);
		return label;
	}

	public static Label createH6Label(String text) {
		Label label = new Label(text);
		label.addClassName(LumoStyles.Heading.H6);
		return label;
	}

	/* === MISC === */

	//	public static String formatAddress(Address address) {
	//		return address.getStreet() + "\n" + address.getCity() + ", "
	//				+ address.getCity() + " " + address.getZip();
	//	}

	public static Button createFloatingActionButton(VaadinIcon icon) {
		Button button = createPrimaryButton(icon);
		button.addThemeName("fab");
		return button;
	}

	//	public static FlexLayout createPhoneLayout() {
	//		TextField prefix = new TextField();
	//		prefix.setValue("+358");
	//		prefix.setWidth("80px");
	//
	//		TextField number = new TextField();
	//		number.setValue(DummyData.getPhoneNumber());
	//
	//		FlexBoxLayout layout = new FlexBoxLayout(prefix, number);
	//		layout.setFlexGrow(1, number);
	//		layout.setSpacing(Right.S);
	//		return layout;
	//	}

	/* === NUMBERS === */

	public static String formatAmount(Double amount) {
		return decimalFormat.get().format(amount);
	}

	public static String formatAmount(int amount) {
		return decimalFormat.get().format(amount);
	}

	public static Label createAmountLabel(double amount) {
		Label label = createH5Label(formatAmount(amount));
		label.addClassName(LumoStyles.FontFamily.MONOSPACE);
		return label;
	}

	public static String formatUnits(int units) {
		return NumberFormat.getIntegerInstance().format(units);
	}

	public static Label createUnitsLabel(int units) {
		Label label = new Label(formatUnits(units));
		label.addClassName(LumoStyles.FontFamily.MONOSPACE);
		return label;
	}

	/* === ICONS === */

	public static Icon createPrimaryIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.PRIMARY, i);
		return i;
	}

	public static Icon createSecondaryIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.SECONDARY, i);
		return i;
	}

	public static Icon createTertiaryIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.TERTIARY, i);
		return i;
	}

	public static Icon createDisabledIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.DISABLED, i);
		return i;
	}

	public static Icon createSuccessIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.SUCCESS, i);
		return i;
	}

	public static Icon createErrorIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		setTextColor(TextColor.ERROR, i);
		return i;
	}

	public static Icon createSmallIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		i.addClassName(IconSize.S.getClassName());
		return i;
	}

	public static Icon createLargeIcon(VaadinIcon icon) {
		Icon i = new Icon(icon);
		i.addClassName(IconSize.L.getClassName());
		return i;
	}

	// Combinations

	public static Icon createIcon(IconSize size, TextColor color,
			VaadinIcon icon) {
		Icon i = new Icon(icon);
		i.addClassNames(size.getClassName());
		setTextColor(color, i);
		return i;
	}

	/* === DATES === */

	public static String formatDate(LocalDate date) {
		return dateFormat.get().format(date);
	}

	/* === NOTIFICATIONS === */

	public static void showNotification(String text) {
		Notification.show(text, 3000, Notification.Position.BOTTOM_CENTER);
	}

	public static void showErrorNotification(String text, int duration, Position position) {
		Notification n = new Notification(text,
				(duration==0? 3000 : duration),
				(position==null?Notification.Position.MIDDLE : position));
		n.addThemeVariants(NotificationVariant.LUMO_ERROR);
		n.open();
	}

	public static void showNotification(String text, int duration, Position position,NotificationVariant theme) {
		Notification n = new Notification(text,
				(duration==0? 3000 : duration),
				(position==null?Notification.Position.TOP_CENTER : position));
		if(theme!=null) {
			n.addThemeVariants(theme);
		}
		n.open();
	}

	/* === CSS UTILITIES === */

	public static void setAlignSelf(AlignSelf alignSelf,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("align-self",
					alignSelf.getValue());
		}
	}

	public static void setBackgroundColor(String backgroundColor,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("background-color",
					backgroundColor);
		}
	}

	public static void setBorderRadius(BorderRadius borderRadius,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("border-radius",
					borderRadius.getValue());
		}
	}

	public static void setBoxSizing(BoxSizing boxSizing,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("box-sizing",
					boxSizing.getValue());
		}
	}

	public static void setColSpan(int span, Component... components) {
		for (Component component : components) {
			component.getElement().setAttribute("colspan",
					Integer.toString(span));
		}
	}

	public static void setFontSize(FontSize fontSize,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("font-size",
					fontSize.getValue());
		}
	}

	public static void setFontWeight(FontWeight fontWeight,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("font-weight",
					fontWeight.getValue());
		}
	}

	public static void setLineHeight(LineHeight lineHeight,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("line-height",
					lineHeight.getValue());
		}
	}

	public static void setLineHeight(String value,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("line-height",
					value);
		}
	}

	public static void setMaxWidth(String value, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("max-width", value);
		}
	}

	public static void setOverflow(Overflow overflow, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("overflow",
					overflow.getValue());
		}
	}

	public static void setPointerEvents(PointerEvents pointerEvents, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("pointer-events",
					pointerEvents.getValue());
		}
	}

	public static void setShadow(Shadow shadow, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("box-shadow",
					shadow.getValue());
		}
	}

	public static void setTextAlign(TextAlign textAlign,
			Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("text-align",
					textAlign.getValue());
		}
	}

	public static void setTextColor(TextColor textColor, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("color", textColor.getValue());
		}
	}

	public static void setTextOverflow(TextOverflow textOverflow, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("text-overflow", textOverflow.getValue());
		}
	}

	public static void setTheme(String theme, Component... components) {
		for (Component component : components) {
			component.getElement().setAttribute("theme", theme);
		}
	}

	public static void setTooltip(String tooltip, Component... components) {
		for (Component component : components) {
			component.getElement().setProperty("title", tooltip);
		}
	}

	public static void setWhiteSpace(WhiteSpace whiteSpace,
			Component... components) {
		for (Component component : components) {
			component.getElement().setProperty("white-space",
					whiteSpace.getValue());
		}
	}

	public static void setWidth(String value, Component... components) {
		for (Component component : components) {
			component.getElement().getStyle().set("width", value);
		}
	}


	/* === ACCESSIBILITY === */

	public static void setAriaLabel(String value, Component... components) {
		for (Component component : components) {
			component.getElement().setAttribute("aria-label", value);
		}
	}

	public static void getLogoImage(Image image) {
		image.setSrc("/img/logo.png");
	}

	public static void getLogoBrandImage(Image image) {
		image.setSrc("/img/logobrand.png");
		image.setWidth(50, Unit.PIXELS);
		image.setHeight(50, Unit.PIXELS);
	}
	
	public static Image getLogoImage() {
		Image image = new Image();
		getLogoImage(image);
		return image;
	}

}