/*
 * Pager.java
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

/**
 * Query string parameter for paged tables. Keeps track of page number and page size.
 * <p>
 * Note: currently cannot be used in Javascript routes.
 */
public class Pager implements QueryStringBindable<Pager> {

    private int pageNr;

    private int pageSize; // public for ease of use in templates

    public int getPageNr() {
        return pageNr;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pager () {
        // must exist for query string binder to work
    }

    public Pager(int pageNr, int pageSize) {
        this.pageNr = pageNr;
        this.pageSize = pageSize;
    }

    public boolean hasPrevious() {
        return pageNr > 0;
    }

    public boolean hasNext(int count) {
        return pageSize*(pageNr+1) < count;
    }

    public Pager next() {
        return new Pager(pageNr + 1, pageSize);
    }

    public Pager previous() {
        return new Pager(hasPrevious() ? pageNr - 1 : 0, pageSize);
    }

    public int getStart () {
        return pageSize * pageNr + 1;
    }

    public int getEnd(int count) {
        return Math.min (count, pageSize*(pageNr+1));
    }

    public Pager getFirst() {
        return new Pager(0, pageSize);
    }

    public Pager resize(int size) {
        return new Pager (pageNr * pageSize / size, size);
    }

    @Override
    public Optional<Pager> bind(String key, Map<String, String[]> data) {
        String[] nrs = data.get(key + ".nr");
        String[] sizes = data.get(key + ".size");
        if (nrs == null || sizes == null || nrs.length != 1 || sizes.length != 1) {
            return Optional.empty();
        } else {
            try {
                pageNr = Integer.parseInt(nrs[0]);
                pageSize = Integer.parseInt(sizes[0]);
                return Optional.of(this);
            } catch (NumberFormatException ex) {
                return Optional.empty();
            }
        }
    }

    @Override
    public String unbind(String key) {
        return key +".nr=" + pageNr + "&" + key + ".size=" + pageSize;
    }

    @Override
    public String javascriptUnbind() {
        return null; // TODO
    }
}


