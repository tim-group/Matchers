package com.youdevise.testutils.matchers.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.regex.Pattern;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import static com.google.common.base.Functions.compose;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;

public class MagicJavaBeanMatcher<T> implements InvocationHandler {
    private final JavaBeanMatcher<T> baseMatcher;

    public static <T> MagicJavaBeanMatcher<T> forClass(Class<T> klass, String description) {
        return forMatcher(new BaseJavaBeanMatcher<T>(klass, description) { });
    }

    public static <T> MagicJavaBeanMatcher<T> forMatcher(BaseJavaBeanMatcher<T> baseMatcher) {
        return forMatcher(new DelegatingJavaBeanMatcher<T>(baseMatcher));
    }

    public static <T> MagicJavaBeanMatcher<T> forMatcher(JavaBeanMatcher<T> baseMatcher) {
        return new MagicJavaBeanMatcher<T>(baseMatcher);
    }

    private MagicJavaBeanMatcher(JavaBeanMatcher<T> baseMatcher) {
        this.baseMatcher = baseMatcher;
    }

    @SuppressWarnings("unchecked")
    public <I> I getProxy(Class<I> iface) {
        return (I) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] {iface}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            return proxy == args[0];
        }

        if (method.getDeclaringClass().isAssignableFrom(JavaBeanMatcher.class)) {
            return method.invoke(baseMatcher, args);
        }

        applyMatch(method, args);

        final Class<?> returnType = method.getReturnType();
        return Proxy.newProxyInstance(returnType.getClassLoader(), new Class<?>[] {returnType}, this);
    }

    public void applyMatch(Method method, Object[] args) throws InstantiationException, IllegalAccessException {
        if (method.isAnnotationPresent(MatchesWith.class)) {
            matchWithCustomMethod(method, args);
            return;
        }

        if (method.isAnnotationPresent(AssertsFlag.class)) {
            final AssertsFlag assertsFlag = method.getAnnotation(AssertsFlag.class);
            baseMatcher.withProperty(assertsFlag.name(), equalTo(assertsFlag.expected()));
            return;
        }

        matchWithPropertyFromMethod(method, args);
    }

    public void matchWithPropertyFromMethod(Method method, Object[] args) {
        final String propertyName = getPropertyNameFromMethod(method);
        final Matcher<?> matcher = getMatcherFromArgs(args);
        baseMatcher.withProperty(propertyName, matcher);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void matchWithCustomMethod(Method method, Object[] args) throws InstantiationException, IllegalAccessException {
        final MatchesWith matchesWith = method.getAnnotation(MatchesWith.class);
        final Class<? extends JavaBeanPropertyMatcherMaker<?>> matcherMakerClass = matchesWith.value();
        final JavaBeanPropertyMatcherMaker matcherMaker = matcherMakerClass.newInstance();
        matcherMaker.makeProperty(baseMatcher, args);
    }

    private static final Pattern WITH_THE_FOO = Pattern.compile("with_the_(.*)");
    private static final Pattern WITH_A_FOO_OF = Pattern.compile("with_a_(.*)_of");
    private static final Pattern WITH_AN_FOO_OF = Pattern.compile("with_an_(.*)_of");
    private static final Pattern WITH_FOO = Pattern.compile("with_(.*)");

    private static Function<Pattern, java.util.regex.Matcher> matchingTheString(final String input) {
        return new Function<Pattern, java.util.regex.Matcher>() {
            @Override
            public java.util.regex.Matcher apply(Pattern pattern) {
                return pattern.matcher(input);
            }
        };
    }

    private static final Function<java.util.regex.Matcher, Optional<String>> TO_FIRST_MATCH
        = new Function<java.util.regex.Matcher, Optional<String>>() {
            @Override public Optional<String> apply(java.util.regex.Matcher matcher) {
                if (!matcher.matches()) {
                    return Optional.absent();
                }
                return Optional.of(matcher.group(1));
            }
        };

    private String getPropertyNameFromMethod(Method method) {
        if (method.isAnnotationPresent(AddressesProperty.class)) {
            return method.getAnnotation(AddressesProperty.class).value();
        }
        final String withUnderscores = firstMatch(method.getName(), WITH_THE_FOO, WITH_A_FOO_OF, WITH_AN_FOO_OF, WITH_FOO)
                .or(throwA(new RuntimeException(String.format("Method name %s does not indicate a property", method.getName()))));
        return stripUnderscores(withUnderscores);
    }

    private static Optional<String> firstMatch(String input, Pattern... patterns) {
        final Iterable<Optional<String>> matching = transform(asList(patterns), compose(TO_FIRST_MATCH, matchingTheString(input)));
        return find(matching, isPresent(String.class), Optional.<String>absent());
    }

    private static <T> Predicate<? super Optional<T>> isPresent(@SuppressWarnings("unused") Class<T> type) {
        return new Predicate<Optional<T>>() {
            @Override public boolean apply(Optional<T> value) {
                return value.isPresent();
            }
        };
    }

    private String stripUnderscores(String withUnderscores) {
        final String camelCased = Joiner.on("").join(transform(asList(withUnderscores.split("_")), UPPERCASE_FIRST_CHAR));
        return new StringBuffer(camelCased.length())
                .append(Character.toLowerCase(camelCased.charAt(0)))
                .append(camelCased.substring(1))
                .toString();
    }

    private static final Function<String, String> UPPERCASE_FIRST_CHAR = new Function<String, String>() {
        @Override public String apply(String input) {
            return new StringBuffer(input.length())
                    .append(Character.toTitleCase(input.charAt(0)))
                    .append(input.substring(1))
                    .toString();
        }
    };

    private Matcher<?> getMatcherFromArgs(Object[] args) {
        final Object firstArg = args[0];
        if (firstArg instanceof Matcher) {
            return (Matcher<?>) firstArg;
        }
        return equalTo(firstArg);
    }

    private static Supplier<String> throwA(final RuntimeException exception) {
        return new Supplier<String>() {
            @Override public String get() {
                throw exception;
            }
        };
    }
}
