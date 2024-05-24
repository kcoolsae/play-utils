/*
 * Sorter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.binders;

import play.mvc.QueryStringBindable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * Query string parameter for paged tables. Keeps track of which column to sort and in which direction
 * <p>
 * Note: currently cannot be used in Javascript routes.
 */
public class Sorter implements QueryStringBindable<Sorter> {

    private String sortColumn;

    private boolean ascending;

    public Sorter(String sortColumn, boolean ascending) {
        this.sortColumn = sortColumn;
        this.ascending = ascending;
    }

    public Sorter() {
        // needed for query string binder to work
    }

    public String getSortColumn() {

        return sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public String getCellClass(String field) {
        return sortColumn.equals(field) ? "sortable" : "";
    }

    public String getColumnClass(String field) {
        if (sortColumn.equals(field)) {
            return ascending ? "sorted asc" : "sorted desc";
        } else {
            return "sortable";
        }
    }

    public Sorter forColumn(String field) {
        if (field.equals(sortColumn)) {
            return new Sorter(sortColumn, !ascending);
        } else {
            return new Sorter(field, true);
        }
    }

    @Override
    public Optional<Sorter> bind(String key, Map<String, String[]> map) {
        String[] values = map.get(key);
        if (values == null || values.length != 1) {
            return Optional.empty();
        } else {
            if (values[0].startsWith("+")) {
                this.ascending = true;
            } else if (values[0].startsWith("-")) {
                this.ascending = false;
            } else {
                return Optional.empty();
            }
            // if correct syntax
            this.sortColumn = values[0].substring(1);
            return Optional.of(this);
        }
    }

    @Override
    public String unbind(String key) {
        return key + "=" + URLEncoder.encode((ascending ? "+" : "-") + sortColumn, StandardCharsets.UTF_8);
    }

    @Override
    public String javascriptUnbind() {
        return null;   // TODO?

    }
}
