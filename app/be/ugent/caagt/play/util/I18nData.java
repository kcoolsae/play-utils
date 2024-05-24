/*
 * I18nData.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Generated form a record class.
//
// Scala compilers that Play uses also for compiling Java does not recognize
// records at top level.
@SuppressWarnings("ClassCanBeRecord")
public final class I18nData {
    private final String key;
    private final Object[] args;

    public I18nData(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    public List<Object> argsAsList() {
        return Arrays.asList(args);
    }

    public String key() {
        return key;
    }

    public Object[] args() {
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof I18nData that) {
            return Objects.equals(this.key, that.key) &&
                Arrays.equals(this.args, that.args);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, Arrays.hashCode(args));
    }

    @Override
    public String toString() {
        return "I18nData[" + "key=" + key + ", " + "args=" + Arrays.toString(args) + ']';
    }

}
