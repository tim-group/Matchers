package com.youdevise.testutils.matchers.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.PropertyUtil.getPropertyDescriptor;

public final class PropertyMatcher extends TypeSafeDiagnosingMatcher<Object> {

    private final String propertyName;
    private final Matcher<?> valueMatcher;

    @Factory
    public static PropertyMatcher an_object_with_property(String propertyName, Matcher<?> valueMatcher) {
        return new PropertyMatcher(propertyName, is(valueMatcher));
    }

    @Factory
    public static PropertyMatcher an_object_whose(String propertyName, Matcher<?> valueMatcher) {
        return new PropertyMatcher(propertyName, valueMatcher);
    }

    public static PropertyMatcherFactory an_object_whose(String propertyName) {
        return new PropertyMatcherFactory(propertyName);
    }

    public static final class PropertyMatcherFactory {
        private final String targetPropertyName;

        private PropertyMatcherFactory(String propertyName) {
            targetPropertyName = propertyName;
        }

        public PropertyMatcher is(Matcher<?> valueMatcher) {
            return new PropertyMatcher(targetPropertyName, Matchers.is(valueMatcher));
        }
    }

    private PropertyMatcher(String propertyName, Matcher<?> valueMatcher) {
        this.propertyName = propertyName;
        this.valueMatcher = valueMatcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("whose ").appendText(propertyName).appendText(" ").appendDescriptionOf(valueMatcher);
    }

    @Override
    protected boolean matchesSafely(Object item, Description mismatchDescription) {
        final PropertyDescriptor descriptor = getPropertyDescriptor(propertyName, item);
        if (null == descriptor) {
            return matchOnPublicField(item, mismatchDescription);
        }

        final Method readMethod = descriptor.getReadMethod();
        if (null == readMethod) {
            mismatchDescription.appendText("with no getter method for the ").appendValue(propertyName).appendText(" property");
            return false;
        }

        Object actualValue = null;
        try {
            actualValue = readMethod.invoke(item);
        } catch (final Exception exception) {
            mismatchDescription
                .appendText("whose ")
                .appendValue(propertyName)
                .appendText(" getter method threw an exception ")
                .appendValue(exception.getCause() == null ? exception : exception.getCause());
            return false;
        }

        return matchOnValue(mismatchDescription, actualValue);
    }

    private boolean matchOnValue(Description mismatchDescription, Object actualValue) {
        mismatchDescription.appendText("whose ").appendText(propertyName).appendText(" ");

        final boolean matches = valueMatcher.matches(actualValue);
        if (!matches) {
            valueMatcher.describeMismatch(actualValue, mismatchDescription);
        }

        return matches;
    }

    private boolean matchOnPublicField(Object item, Description mismatchDescription) {
        final Field field = getField(item, mismatchDescription);
        if (field == null) {
            return false;
        }
        Object value = null;
        try {
            value = getValue(item, field);
        } catch (final Exception exception) {
            mismatchDescription
                .appendText("whose ")
                .appendValue(propertyName)
                .appendText(" field could not be read, due to:")
                .appendValue(exception.getCause() == null ? exception : exception.getCause());
            return false;
        }
        return matchOnValue(mismatchDescription, value);
    }

    private Field getField(Object item, Description mismatchDescription) {
        try {
            return findField(item.getClass());
        } catch (final NoSuchFieldException exception) {
            mismatchDescription
            .appendText("with no public property or field named ")
            .appendValue(propertyName);
            return null;
        } catch (final SecurityException exception) {
            mismatchDescription
                .appendText("which could not be reflected upon to find  ")
                .appendValue(propertyName)
                .appendText(" due to:")
                .appendValue(exception.getCause() == null ? exception : exception.getCause());
            return null;
        }
    }

    public Field findField(Class<?> clazz) throws NoSuchFieldException {
        if (clazz == Object.class) {
            throw new NoSuchFieldException();
        }

        try {
            return clazz.getDeclaredField(propertyName);
        } catch (final NoSuchFieldException e) {
            return findField(clazz.getSuperclass());
        }
    }

    private Object getValue(Object item, Field field) throws Exception {
        return field.get(item);
    }
}
