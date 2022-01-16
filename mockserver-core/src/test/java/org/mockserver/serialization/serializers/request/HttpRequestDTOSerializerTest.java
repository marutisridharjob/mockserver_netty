package org.mockserver.serialization.serializers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.mockserver.model.Cookie;
import org.mockserver.model.Header;
import org.mockserver.model.JsonSchemaBody;
import org.mockserver.model.XmlSchemaBody;
import org.mockserver.serialization.ObjectMapperFactory;
import org.mockserver.serialization.model.HttpRequestDTO;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockserver.character.Character.NEW_LINE;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.JsonBody.json;
import static org.mockserver.model.JsonPathBody.jsonPath;
import static org.mockserver.model.JsonSchemaBody.jsonSchema;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;
import static org.mockserver.model.RegexBody.regex;
import static org.mockserver.model.XPathBody.xpath;
import static org.mockserver.model.XmlBody.xml;
import static org.mockserver.model.XmlSchemaBody.xmlSchema;

public class HttpRequestDTOSerializerTest {

    private final ObjectWriter objectMapper = ObjectMapperFactory.createObjectMapper(true);

    @Test
    public void shouldReturnJsonWithNoFieldsSet() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
            )),
            is("{ }"));
    }

    @Test
    public void shouldReturnJsonWithAllFieldsSet() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody("some_body")
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
                    .withKeepAlive(true)
                    .withSecure(true)
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"keepAlive\" : true," + NEW_LINE +
                "  \"secure\" : true," + NEW_LINE +
                "  \"body\" : \"some_body\"" + NEW_LINE +
                "}"));
    }

    @Test
    public void shouldReturnJsonWithJsonBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(json("{ \"key\": \"some_value\" }"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"key\" : \"some_value\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}"));
    }

    @Test
    public void shouldReturnJsonWithJsonSchemaBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(new JsonSchemaBody("{\"type\": \"object\", \"properties\": {\"id\": {\"type\": \"integer\"}}, \"required\": [\"id\"]}")),
            is("{" + NEW_LINE +
                "  \"type\" : \"JSON_SCHEMA\"," + NEW_LINE +
                "  \"jsonSchema\" : {" + NEW_LINE +
                "    \"type\" : \"object\"," + NEW_LINE +
                "    \"properties\" : {" + NEW_LINE +
                "      \"id\" : {" + NEW_LINE +
                "        \"type\" : \"integer\"" + NEW_LINE +
                "      }" + NEW_LINE +
                "    }," + NEW_LINE +
                "    \"required\" : [ \"id\" ]" + NEW_LINE +
                "  }" + NEW_LINE +
                "}"));

        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(jsonSchema("{\"type\": \"object\", \"properties\": {\"id\": {\"type\": \"integer\"}}, \"required\": [\"id\"]}"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"JSON_SCHEMA\"," + NEW_LINE +
                "    \"jsonSchema\" : {" + NEW_LINE +
                "      \"type\" : \"object\"," + NEW_LINE +
                "      \"properties\" : {" + NEW_LINE +
                "        \"id\" : {" + NEW_LINE +
                "          \"type\" : \"integer\"" + NEW_LINE +
                "        }" + NEW_LINE +
                "      }," + NEW_LINE +
                "      \"required\" : [ \"id\" ]" + NEW_LINE +
                "    }" + NEW_LINE +
                "  }" + NEW_LINE +
                "}"));
    }

    @Test
    public void shouldReturnJsonWithJsonPathBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(jsonPath("$..book[?(@.price <= $['expensive'])]"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"JSON_PATH\"," + NEW_LINE +
                "    \"jsonPath\" : \"$..book[?(@.price <= $['expensive'])]\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}")
        );
    }

    @Test
    public void shouldReturnJsonWithXmlBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(xml("<some><xml></xml></some>"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"XML\"," + NEW_LINE +
                "    \"xml\" : \"<some><xml></xml></some>\"," + NEW_LINE +
                "    \"rawBytes\" : \"PHNvbWU+PHhtbD48L3htbD48L3NvbWU+\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}")
        );
    }

    @Test
    public void shouldReturnJsonWithXmlSchemaBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(new XmlSchemaBody("{\"type\": \"object\", \"properties\": {\"id\": {\"type\": \"integer\"}}, \"required\": [\"id\"]}")),
            is("{" + NEW_LINE +
                "  \"type\" : \"XML_SCHEMA\"," + NEW_LINE +
                "  \"xmlSchema\" : \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"id\\\": {\\\"type\\\": \\\"integer\\\"}}, \\\"required\\\": [\\\"id\\\"]}\"" + NEW_LINE +
                "}"));

        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(xmlSchema("{\"type\": \"object\", \"properties\": {\"id\": {\"type\": \"integer\"}}, \"required\": [\"id\"]}"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"XML_SCHEMA\"," + NEW_LINE +
                "    \"xmlSchema\" : \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"id\\\": {\\\"type\\\": \\\"integer\\\"}}, \\\"required\\\": [\\\"id\\\"]}\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}"));
    }

    @Test
    public void shouldReturnJsonWithXPathBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(xpath("//some/xml/path"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"XPATH\"," + NEW_LINE +
                "    \"xpath\" : \"//some/xml/path\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}")
        );
    }

    @Test
    public void shouldReturnJsonWithRegexBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(regex("[a-z]{1,3}"))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"REGEX\"," + NEW_LINE +
                "    \"regex\" : \"[a-z]{1,3}\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "}")
        );
    }

    @Test
    public void shouldReturnJsonWithParameterBodyInToString() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(
            new HttpRequestDTO(
                request()
                    .withMethod("GET")
                    .withPath("/some/path")
                    .withPathParameters(param("path_parameterOneName", "path_parameterOneValue"))
                    .withQueryStringParameters(param("parameterOneName", "parameterOneValue"))
                    .withBody(params(
                        param("path_parameterOneName", "path_parameterOneValueOne", "path_parameterOneValueTwo"),
                        param("path_parameterTwoName", "path_parameterTwoValue")
                    ))
                    .withHeaders(new Header("name", "value"))
                    .withCookies(new Cookie("name", "[A-Z]{0,10}"))
            )
            ),
            is("{" + NEW_LINE +
                "  \"method\" : \"GET\"," + NEW_LINE +
                "  \"path\" : \"/some/path\"," + NEW_LINE +
                "  \"pathParameters\" : {" + NEW_LINE +
                "    \"path_parameterOneName\" : [ \"path_parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"queryStringParameters\" : {" + NEW_LINE +
                "    \"parameterOneName\" : [ \"parameterOneValue\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"headers\" : {" + NEW_LINE +
                "    \"name\" : [ \"value\" ]" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"cookies\" : {" + NEW_LINE +
                "    \"name\" : \"[A-Z]{0,10}\"" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"body\" : {" + NEW_LINE +
                "    \"type\" : \"PARAMETERS\"," + NEW_LINE +
                "    \"parameters\" : {" + NEW_LINE +
                "      \"path_parameterTwoName\" : [ \"path_parameterTwoValue\" ]," + NEW_LINE +
                "      \"path_parameterOneName\" : [ \"path_parameterOneValueOne\", \"path_parameterOneValueTwo\" ]" + NEW_LINE +
                "    }" + NEW_LINE +
                "  }" + NEW_LINE +
                "}")
        );
    }

}
