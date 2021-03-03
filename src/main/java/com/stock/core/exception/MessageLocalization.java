package com.stock.core.exception;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageLocalization {

	private static final String BASE_NAME = "thgames_error_message";

	private static Locale resourceBundleLocale = null;
	private static ResourceBundle resourceBundle = null;

	private static Locale locale = null;

	public static final Locale getLocale() {
		return MessageLocalization.locale == null ? Locale.SIMPLIFIED_CHINESE : MessageLocalization.locale;
	}

	public static final void setLocale(final Locale newLocale) {
		MessageLocalization.locale = newLocale;
	}

	private static synchronized final ResourceBundle getLocalBundle() {
		Locale currentLocale = MessageLocalization.getLocale();
		if (!currentLocale.equals(MessageLocalization.resourceBundleLocale)) {
			MessageLocalization.resourceBundleLocale = currentLocale;
			MessageLocalization.resourceBundle = ResourceBundle.getBundle(MessageLocalization.BASE_NAME,
					MessageLocalization.resourceBundleLocale, MessageLocalization.class.getClassLoader());
		}
		return MessageLocalization.resourceBundle;
	}

	public static final String getString(final String key) {
		try {
			return MessageLocalization.getLocalBundle().getString(key);
		} catch (Exception e) {
			return String.format("<%s>", key);
		}
	}

}
