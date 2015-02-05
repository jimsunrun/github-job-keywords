package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * QueryKey represents the unique parameters for a query expression and a location.
 * <p/>
 * 
 * This class is a subset of SearchParameters. This subset is needed because results from multipe
 * searches can be accumulated for a given query-string and location. Indeed will only return a max
 * of 25 jobs per search. So the application allows the user to search across several pages of
 * results in order to span more than 25 jobs.
 * <p/>

 * The QueryTermRepository accumulates results from multiple searches. The QueryKey is the index
 * into the repository's map of unique search expressions for a given city and country.
 * <p/>
 * 
 * QueryKey is an immutable class that can be used an an index in a Map.
 * <p/>
 * 
 * This class also defines a public Comparator that allow the UI to sort a list of QueryKeys.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public class QueryKey {

    private String query;
    private Locale locale;
    private String city;

    private HashCodeBuilder hashCodeBuilder;
    private String toStringCache;

    public QueryKey(String query, Locale locale, String city) {
        super();
        this.query = query;
        this.locale = locale;
        this.city = city;

        hashCodeBuilder = new HashCodeBuilder(17, 37).append(query).append(locale).append(city);

        StringBuilder builder = new StringBuilder("Search-params: ");
        builder.append(locale == null ? "" : locale + ", ");
        builder.append(city == null ? "" : city + ", ");
        builder.append(query == null ? "'" : query + "', ");
        toStringCache = builder.toString();
    }

    @Override
    public int hashCode() {
        return hashCodeBuilder.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        QueryKey otherSearchParameters = (QueryKey) other;

        // TODO cache the EqualsBuilder for performance ! ! !
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(query, otherSearchParameters.query).append(locale, otherSearchParameters.locale)
            .append(city, otherSearchParameters.city);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return toStringCache;
    }

    public String getQuery() {
        return query;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCity() {
        return city;
    }

    /**
     * This comparator sorts first by Query-string, then by Country (aka Locale), then by City.
     */
    public static class QueryKeyComparator implements Comparator<QueryKey> {

        @Override
        public int compare(QueryKey key1, QueryKey key2) {
            // The CompareToBuilder can't be cached - which is OK because this Comparator isn't used
            // often
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(key1.query, key2.query);
            builder.append((key1.locale != null) ? key1.locale.getCountry() : null,
                (key2.locale != null) ? key2.locale.getCountry() : null);
            builder.append(key1.city, key2.city);
            return builder.build();
        }
    }

}
