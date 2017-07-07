package com.youdevise.testutils.matchers.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.youdevise.testutils.matchers.httpclient.HttpResponseMatchers.jsonContent;
import static com.youdevise.testutils.matchers.json.JsonStructureMatchers.jsonObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class HttpEntityMatchersTest {
    @Test
    public void matching_http_entity_containing_json_shows_actual_json_in_mismatch_description() throws Exception {
        BasicHttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0, 200, "OK"));
        StringEntity entity = new StringEntity("{ \"a\" : 1 }");
        entity.setContentType("application/json");
        httpResponse.setEntity(entity);

        Matcher<HttpResponse> matcher = jsonContent(jsonObject());
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(httpResponse, mismatchDescription);
        assertThat(mismatchDescription.toString(), containsString("Actual JSON was: <{\"a\":1}>"));
    }
}