package com.nguyen.i18n.clazz;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class URLLocaleResolver implements LocaleResolver {

    private static final String DEFAULT_PARAM_NAME = "lang";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final String DEFAULT_ATTRIBUTE_NAME = URLLocaleResolver.class.getName() + ".LOCALE";

    private List<Locale> locales = new ArrayList<>();
    private Locale defaultLocale = DEFAULT_LOCALE;

    public URLLocaleResolver(Locale... Locales) {
        this.locales.addAll(Arrays.asList(Locales));
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        parseLocaleCookieIfNecessary(request);
        Locale locale = (Locale) request.getAttribute(DEFAULT_ATTRIBUTE_NAME);
        setLocale(request, locale);

        return locale;
    }

    private void parseLocaleCookieIfNecessary(HttpServletRequest request) {
        Locale locale = getLocaleFromRequest(request);
        if (locale == null) {
            locale = defaultLocale;
        }

        request.setAttribute(DEFAULT_ATTRIBUTE_NAME, locale);
    }

    private Locale getLocaleFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return locales.stream().
                filter(locale -> uri.startsWith("/" + locale.getLanguage() + "/")).
                findFirst().orElse(null);
    }

    public void setLocale(HttpServletRequest request, Locale locale) {
        setLocale(request, null, locale);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if (locale != null) {
            request.setAttribute(DEFAULT_PARAM_NAME, locale.getLanguage());
        }
    }
}
