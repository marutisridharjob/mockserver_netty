package org.mockserver.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.mock.Expectation;
import org.mockserver.model.*;
import org.mockserver.serialization.model.*;
import org.mockserver.validator.jsonschema.JsonSchemaExpectationValidator;

import java.io.IOException;
import java.util.Collections;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockserver.model.Cookie.cookie;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpTemplate.template;
import static org.mockserver.model.NottableString.string;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.StringBody.exact;

/**
 * @author jamesdbloom
 */
public class ExpectationWithResponseTemplateSerializerTest {

    private final Expectation fullExpectation = new Expectation(
        new HttpRequest()
            .withMethod("GET")
            .withPath("somePath")
            .withQueryStringParameters(new Parameter("queryParameterName", Collections.singletonList("queryParameterValue")))
            .withBody(new StringBody("someBody"))
            .withHeaders(new Header("headerName", "headerValue"))
            .withCookies(new Cookie("cookieName", "cookieValue")),
        Times.once(),
        TimeToLive.exactly(HOURS, 2L),
        10)
        .thenRespond(
            template(HttpTemplate.TemplateType.JAVASCRIPT, "some_random_template")
                .withDelay(SECONDS, 5)
        );
    private final ExpectationDTO fullExpectationDTO = new ExpectationDTO()
        .setHttpRequest(
            new HttpRequestDTO()
                .setMethod(string("GET"))
                .setPath(string("somePath"))
                .setQueryStringParameters(new Parameters().withEntries(
                    param("queryParameterName", "queryParameterValue")
                ))
                .setBody(new StringBodyDTO(exact("someBody")))
                .setHeaders(new Headers().withEntries(
                    header("headerName", "headerValue")
                ))
                .setCookies(new Cookies().withEntries(
                    cookie("cookieName", "cookieValue")
                ))
        )
        .setHttpResponseTemplate(
            new HttpTemplateDTO(
                new HttpTemplate(HttpTemplate.TemplateType.JAVASCRIPT)
                    .withTemplate("some_random_template")
                    .withDelay(new Delay(SECONDS, 5))
            )
        )
        .setTimes(new org.mockserver.serialization.model.TimesDTO(Times.once()))
        .setTimeToLive(new TimeToLiveDTO(TimeToLive.exactly(HOURS, 2L)))
        .setPriority(10);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ObjectWriter objectWriter;
    @Mock
    private JsonArraySerializer jsonArraySerializer;
    @Mock
    private JsonSchemaExpectationValidator expectationValidator;

    @InjectMocks
    private ExpectationSerializer expectationSerializer = new ExpectationSerializer(new MockServerLogger());


    @Before
    public void setupTestFixture() {
        openMocks(this);
    }

    @Test
    public void shouldSerializeObject() throws IOException {
        // when
        expectationSerializer.serialize(fullExpectation);

        // then
        verify(objectWriter).writeValueAsString(fullExpectationDTO);
    }

    @Test
    public void shouldSerializeArray() throws IOException {
        // when
        expectationSerializer.serialize(fullExpectation, fullExpectation);

        // then
        verify(objectWriter).writeValueAsString(new ExpectationDTO[]{fullExpectationDTO, fullExpectationDTO});
    }

    @Test
    public void shouldDeserializeObject() throws IOException {
        // given
        when(objectMapper.readValue(eq("requestBytes"), same(ExpectationDTO.class))).thenReturn(fullExpectationDTO);
        when(expectationValidator.isValid("requestBytes")).thenReturn("");

        // when
        Expectation expectation = expectationSerializer.deserialize("requestBytes");

        // then
        assertThat(expectation, is(fullExpectation));
    }

    @Test
    public void shouldDeserializeObjectWithError() throws IOException {
        // given
        when(objectMapper.readValue(eq("requestBytes"), same(ExpectationDTO.class))).thenReturn(fullExpectationDTO);
        when(expectationValidator.isValid("requestBytes")).thenReturn("an error");

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("an error");

        // when
        expectationSerializer.deserialize("requestBytes");
    }
}
