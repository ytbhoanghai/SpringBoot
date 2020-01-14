package com.nguyen.i180.clazz;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private Map<Locale, DataSource> targetDataSources;
    private Locale defaultLocale;

    public RoutingDataSource(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
        targetDataSources = new HashMap<>();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.
                        requireNonNull(RequestContextHolder.getRequestAttributes())).
                        getRequest();

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        assert localeResolver != null;
        Locale locale = localeResolver.resolveLocale(request);

        if (targetDataSources.containsKey(locale)) {
            return locale;
        }
        return defaultLocale;
    }

    public void initDataSources(HashMap<Locale, DataSource> map) {
        targetDataSources = map;
        HashMap<Object, Object> dataSources = new HashMap<>();

        map.forEach(dataSources::put);
        this.setTargetDataSources(dataSources);
    }
}
