/*
 * Forms.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package views.html.be.ugent.caagt.play.ext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import play.data.Form;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper functions for forms.
 * <p>Among these helper functions there are several that help with forms with repeating fields.
 * These functions examine a form
 * that is either filled in by being bound to a request, or is associated to an initialized data object.
 * They search for fields with names of the form {@code key[index]} or {@code key[index].something} and determine what indices
 * are encountered.
 * </p>
 */
public final class Forms {

    /**
     * Creates a field with minimal support, not associated with a form. Can be useful as a parameter
     * to template functions that display fields in a case where the associated form is not (entirely) available,
     * e.g., in Ajax calls that are used to extend a form with repeating fields.
     */
    public static Form.Field field(String name, String value) {
        return new Form.Field(null, name, Collections.emptyList(), null, Collections.emptyList(), value);
    }

    private static String extractKey(String str, String extendedKeyName) {
        // TODO do this with Optional in Java 9 (and use flatMap(optional.toStream)?

        if(str.startsWith(extendedKeyName)) {
            int pos = str.indexOf(']');
            if(pos < 0) {
                return null;
            } else {
                return str.substring(extendedKeyName.length(), pos);
            }
        } else {
            return null;
        }
    }


    private static <T> List<Integer> getIntKeysFromFormData(Form<T> form, String key) {
        String extendedKeyName = key + "[";
        return form.rawData().keySet().stream()
            .map(k -> extractKey(k, extendedKeyName))
            .filter(Objects::nonNull).map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    private static <T> List<String> getStringKeysFromFormData(Form<T> form, String key) {
        String extendedKeyName = key + "[";
        return form.rawData().keySet().stream()
            .map(k -> extractKey(k, extendedKeyName))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static <T> List<String> stringMapKeys(Form<T> form, String mapName) {
        // first see whether the form data contains fields of the correct form
        Collection<String> keys = getStringKeysFromFormData(form, mapName);

        if(keys.isEmpty()) {
            // if not, try to retrieve the information from the data backing the form
            if(form.value().isPresent()) {
                T data = form.value().get();
                BeanWrapper beanWrapper = new BeanWrapperImpl(data);
                if(beanWrapper.isReadableProperty(mapName)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) beanWrapper.getPropertyValue(mapName);
                    if(map != null) {
                        keys = map.keySet();
                    }
                }
            }
        }

        // now sort and remove duplicates
        TreeSet<String> set = new TreeSet<>(keys);
        return new ArrayList<>(set);
    }

    /**
     * Retrieve the keys of all fields in the given form corresponding to a map with the given name.
     * The indices are sorted numerically, assuming that the map keys are integers.
     * <p>
     * Note: Sorting is done according
     * to the <i>absolute value</i> of the key to allow a negative key to represent an entry to be deleted.
     * Duplicate indices are removed
     */
    public static <T> List<Integer> intMapKeys(Form<T> form, String mapName) {
        // first see whether the form data contains fields of the correct form
        Collection<Integer> keys = getIntKeysFromFormData(form, mapName);

        if(keys.isEmpty()) {
            // if not, try to retrieve the information from the data backing the form
            if(form.value().isPresent()) {
                T data = form.value().get();
                BeanWrapper beanWrapper = new BeanWrapperImpl(data);
                if(beanWrapper.isReadableProperty(mapName)) {
                    @SuppressWarnings("unchecked")
                    Map<Integer, Object> map = (Map<Integer, Object>) beanWrapper.getPropertyValue(mapName);
                    if(map != null) {
                        keys = map.keySet();
                    }
                }
            }
        }

        // now sort and remove duplicates
        TreeSet<Integer> set = new TreeSet<>(Comparator.comparingInt(Math::abs));
        set.addAll(keys);
        return new ArrayList<>(set);
    }

    /**
     * Retrieve all fields from the given form that correspond to the map with the given name.
     * Similar to {@link #stringMapKeys(Form, String)} but returns fields with names {@code key[...]}.
     * Cannot be used for fields of the more complicated form  {@code key[...].something}
     */
    public static <T> List<Form.Field> intMapFields(Form<T> form, String mapName) {
        return intMapKeys(form, mapName).stream()
            .map(index -> form.field(mapName + "[" + index + "]"))
            .collect(Collectors.toList());
    }

    /**
     * Retrieve the indices of all fields in the given form corresponding to an array with the given name.
     * The indices are sorted numerically, assuming that the map keys are integers. Duplicate indices are removed
     */
    public static <T> List<Integer> arrayIndices(Form<T> form, String arrayName) {
        // first see whether the form data contains fields of the correct form
        Collection<Integer> keys = getIntKeysFromFormData(form, arrayName);

        if(keys.isEmpty()) {
            // if not, try to retrieve the information from the data backing the form
            if(form.value().isPresent()) {
                T data = form.value().get();
                BeanWrapper beanWrapper = new BeanWrapperImpl(data);
                if(beanWrapper.isReadableProperty(arrayName)) {
                    Object[] array = (Object[]) beanWrapper.getPropertyValue(arrayName);
                    if(array != null) {
                        List<Integer> indices = new ArrayList<>(array.length);
                        for(int i = 0; i < array.length; i++) {
                            indices.add(i);
                        }
                        return indices; // already sorted!
                    }
                }
            }
        }

        // now sort and remove duplicates
        return new ArrayList<>(new TreeSet<>(keys));

    }


    /**
     * Retrieve all fields from the given form that correspond to the array with the given name.
     * Similar to {@link #arrayIndices(Form, String)} but returns fields with names {@code key[...]}.
     * Cannot be used for fields of the more complicated form  {@code key[...].something}
     */
    public static <T> List<Form.Field> arrayFields(Form<T> form, String arrayName) {
        return arrayIndices(form, arrayName).stream()
            .map(index -> form.field(arrayName + "[" + index + "]"))
            .collect(Collectors.toList());
    }

    /**
     * Retrieve the indices of all fields in the given form corresponding to a list with the given name.
     * The indices are sorted numerically, assuming that the map keys are integers. Duplicate indices are removed
     */
    public static <T> List<Integer> listIndices(Form<T> form, String listName) {
        // first see whether the form data contains fields of the correct form
        Collection<Integer> keys = getIntKeysFromFormData(form, listName);

        if(keys.isEmpty()) {
            // if not, try to retrieve the information from the data backing the form
            if(form.value().isPresent()) {
                T data = form.value().get();
                BeanWrapper beanWrapper = new BeanWrapperImpl(data);
                if(beanWrapper.isReadableProperty(listName)) {
                    List<?> list = (List<?>) beanWrapper.getPropertyValue(listName);
                    if(list != null) {
                        List<Integer> indices = new ArrayList<>(list.size());
                        for(int i = 0; i < list.size(); i++) {
                            indices.add(i);
                        }
                        return indices;
                    }
                }
            }
        }

        // now sort and remove duplicates
        return new ArrayList<>(new TreeSet<>(keys));
    }

    /**
     * Retrieve all fields from the given form that correspond to the list with the given name.
     * Similar to {@link #listIndices(Form, String)} but returns fields with names {@code key[...]}.
     * Cannot be used for fields of the more complicated form  {@code key[...].something}
     */
    public static <T> List<Form.Field> listFields(Form<T> form, String listName) {
        return listIndices(form, listName).stream()
            .map(index -> form.field(listName + "[" + index + "]"))
            .collect(Collectors.toList());
    }

}
