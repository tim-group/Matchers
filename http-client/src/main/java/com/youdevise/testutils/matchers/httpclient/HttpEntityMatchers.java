package com.youdevise.testutils.matchers.httpclient;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import static com.youdevise.testutils.matchers.httpclient.ContentTypeMatchers.mimeType;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;

public final class HttpEntityMatchers {
    private static final JsonFactory JSON_FACTORY = new MappingJsonFactory()
            .enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

    public static <T extends TreeNode> Matcher<HttpEntity> json(Matcher<? super T> contentMatcher) {
        return new HttpContentMatcher<T>(mimeType(equalTo("application/json")), diagnoseJson(contentMatcher)) {
            @Override
            protected T doParse(HttpEntity item) throws IOException {
                return JSON_FACTORY.createParser(item.getContent()).readValueAsTree();
            }
        };
    }

    public static Matcher<HttpEntity> validXml(Matcher<? super org.w3c.dom.Document> contentMatcher) {
        return new HttpContentMatcher<org.w3c.dom.Document>(mimeType(either(equalTo("application/xml")).or(equalTo("text/xml"))), contentMatcher) {
            @Override
            protected org.w3c.dom.Document doParse(HttpEntity item) throws IOException {
                try {
                    return (DOMDocument) new SAXReader(DOMDocumentFactory.getInstance()).read(item.getContent());
                } catch (org.dom4j.DocumentException e) {
                    throw new AssertionError("Invalid XML document", e);
                }
            }
        };
    }

    public static Matcher<HttpEntity> validXmlDom4J(Matcher<? super org.dom4j.Document> contentMatcher) {
        return new HttpContentMatcher<org.dom4j.Document>(mimeType(either(equalTo("application/xml")).or(equalTo("text/xml"))), contentMatcher) {
            @Override
            protected org.dom4j.Document doParse(HttpEntity item) throws IOException {
                try {
                    return new SAXReader().read(item.getContent());
                } catch (org.dom4j.DocumentException e) {
                    throw new AssertionError("Invalid XML document", e);
                }
            }
        };
    }

    public static Matcher<HttpEntity> validXmlXOM(Matcher<? super nu.xom.Document> contentMatcher) {
        return new HttpContentMatcher<nu.xom.Document>(mimeType(either(equalTo("application/xml")).or(equalTo("text/xml"))), contentMatcher) {
            @Override
            protected nu.xom.Document doParse(HttpEntity item) throws IOException {
                try {
                    return new nu.xom.Builder().build(item.getContent());
                } catch (nu.xom.ParsingException e) {
                    throw new AssertionError("Invalid XML document", e);
                }
            }
        };
    }

    public static Matcher<HttpEntity> html(Matcher<? super String> contentMatcher) {
        return new HttpContentMatcher<String>(mimeType(equalTo("text/html")), contentMatcher) {
            @Override
            protected String doParse(HttpEntity item) throws IOException {
                return EntityUtils.toString(item);
            }
        };
    }

    public static Matcher<HttpEntity> htmlInUtf8(Matcher<? super String> contentMatcher) {
        return new HttpContentMatcher<String>(mimeType(equalTo("text/html")), contentMatcher) {
            @Override
            protected String doParse(HttpEntity item) throws IOException {
                return new String(ByteStreams.toByteArray(item.getContent()), UTF_8);
            }
        };
    }

    public static Matcher<HttpEntity> plainText(Matcher<? super String> contentMatcher) {
        return new HttpContentMatcher<String>(mimeType(equalTo("text/plain")), contentMatcher) {
            @Override
            protected String doParse(HttpEntity item) throws Exception {
                return EntityUtils.toString(item);
            }
        };
    }

    public static Matcher<HttpEntity> plainTextInUtf8(Matcher<? super String> contentMatcher) {
        return new HttpContentMatcher<String>(mimeType(equalTo("text/plain")), contentMatcher) {
            @Override
            protected String doParse(HttpEntity item) throws IOException {
                return new String(ByteStreams.toByteArray(item.getContent()), UTF_8);
            }
        };
    }

    public static Matcher<HttpEntity> contentType(final Matcher<? super ContentType> typeMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpEntity>() {
            @Override
            protected boolean matchesSafely(HttpEntity item, Description mismatchDescription) {
                ContentType contentType = ContentType.get(item);
                if (contentType == null) {
                    mismatchDescription.appendText("no content type");
                } else {
                    mismatchDescription.appendText("content type ");
                    typeMatcher.describeMismatch(contentType, mismatchDescription);
                }
                return typeMatcher.matches(contentType);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("content type ").appendDescriptionOf(typeMatcher);
            }
        };
    }

    public static Matcher<HttpEntity> md5(Matcher<? super String> digestMatcher) {
        return new HttpContentMatcher<String>(any(ContentType.class), digestMatcher) {
            @Override
            protected String doParse(HttpEntity item) throws IOException {
                try (InputStream content = item.getContent()) {
                    return ByteStreams.readBytes(content, MD5_HEX);
                }
            }
        };
    }

    private static final ByteProcessor<String> MD5_HEX = new ByteProcessor<String>() {
        private final MessageDigest digest;

        {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean processBytes(byte[] buf, int off, int len) throws IOException {
            digest.update(buf, off, len);
            return true;
        }

        @Override
        public String getResult() {
            byte[] data = digest.digest();
            StringBuilder builder = new StringBuilder(data.length * 2);
            try (Formatter formatter = new Formatter(builder)) {
                for (int i = 0; i < data.length; i++) {
                    formatter.format("%02x", data[i]);
                }
            }
            return builder.toString();
        }
    };

    private static <T extends TreeNode> Matcher<T> diagnoseJson(Matcher<? super T> contentMatcher) {
        return new TypeSafeDiagnosingMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                contentMatcher.describeMismatch(item, mismatchDescription);
                mismatchDescription.appendText("\nActual JSON was: ").appendValue(item);
                return contentMatcher.matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(contentMatcher);
            }
        };
    }

    private HttpEntityMatchers() {
    }
}
