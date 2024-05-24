/*
 * Tab.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.util;

import play.twirl.api.Html;

import java.util.Collection;
import java.util.List;

/**
 * Defines a tab and its corresponding pane for use in @@tabs template
 */
public class Tab {

    private boolean active;

    private final Object title;// Html or String

    private final String id;

    private final Html content;

    public Tab(Html title, String id, Html content, boolean active) {
        this.title = title;
        this.id = id;
        this.content = content;
        this.active = active;
    }

    public Tab(String titleString, String id, Html content, boolean active) {
        this.title = titleString;
        this.id = id;
        this.content = content;
        this.active = active;
    }

    public Tab(String titleString, String id, Html content) {
        this(titleString, id, content, false);
    }

    public Tab(Html title, String id, Html content) {
        this(title, id, content, false);
    }

    public boolean isActive() {
        return active;
    }

    public Object getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Html getContent() {
        return content;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static void activate(Collection<Tab> tabs, String id) {
        for (Tab tab : tabs) {
            tab.setActive(tab.id.equals(id));
        }
    }

    public static int indexOf(List<Tab> tabs, String id) {
        for (int i = 0; i < tabs.size(); i++) {
            if (id.equals(tabs.get(i).id)) {
                return i;
            }
        }
        return -1;
    }
}
