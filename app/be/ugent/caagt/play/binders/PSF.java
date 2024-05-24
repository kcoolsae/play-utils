/*
 * PSF.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.binders;

import play.mvc.QueryStringBindable;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Combines pager sorter and string map (filter) into a single binder
 */
public class PSF implements QueryStringBindable<PSF> {

    private final Pager pager;

    private final Sorter sorter;

    private final StringMap filter;

    public PSF(Pager pager, Sorter sorter, StringMap filter) {
        this.pager = pager;
        this.sorter = sorter;
        this.filter = filter;
    }

    public PSF() {
        this (new Pager(), new Sorter(), new StringMap());
    }

    /**
     * Initial PSF
     */
    public PSF(String sortColumn, boolean asc, int pageSize) {
        this(new Pager(0,pageSize), new Sorter(sortColumn, asc), new StringMap());
    }

    public StringMap getFilter() {
        return filter;
    }

    public Sorter getSorter() {
        return sorter;
    }

    public Pager getPager() {
        return pager;
    }

    @Override
    public Optional<PSF> bind(String s, Map<String, String[]> map) {
        if (pager.bind(s+".p", map).isPresent() &&
                sorter.bind(s+".s", map).isPresent() &&
                filter.bind(s+".f",map).isPresent()) {
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String unbind(String s) {
        String part1 = pager.unbind(s + ".p") + "&" + sorter.unbind(s + ".s");
        String part2 = filter.unbind(s + ".f");
        if (part2 == null || part2.trim().isEmpty()) {
            return part1;
        } else {
            return part1 + "&" + part2;
        }

    }

    @Override
    public String javascriptUnbind() {
        return null;
    }

    /*
     * Convenience methods
     * ===================
     */

    public PSF previous() {
        return new PSF (pager.previous(), sorter, filter);
    }
    public PSF next() {
        return new PSF (pager.next(), sorter, filter);
    }
    public PSF resort(String field) {
        return new PSF(pager,sorter.forColumn(field),filter);
    }
    public PSF resize(int size) {
        return new PSF (pager.resize(size), sorter, filter);
    }
    public PSF refilter(StringMap newFilter) {
        return new PSF (pager.getFirst(), sorter, newFilter);
    }

    /**
     * Returns an optional filter value. Null or blank values are reported as not present.
     */
    public Optional<String> getFilterValue(String key) {
        String value = filter.get(key);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        } else {
            return Optional.of(value);
        }
    }

    /**
     * Returns an optional integer filter value. Null, blank or non-integer values are reported as not present
     */
    public OptionalInt getFilterValueAsInt(String key) {
        String value = filter.get(key);
        if (value == null || value.isBlank()) {
            return OptionalInt.empty();
        } else {
            try {
                return OptionalInt.of(Integer.parseInt(value.trim()));
            } catch (NumberFormatException ex) {
                return OptionalInt.empty();
            }
        }
    }

    public boolean isAscending() {
        return sorter.isAscending();
    }
    public String getSortColumn() {
        return sorter.getSortColumn();
    }
    public int getPageSize() {
        return pager.getPageSize();
    }
    public int getPageNr() {
        return pager.getPageNr();
    }
}
