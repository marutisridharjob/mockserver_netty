package org.mockserver.model;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MediaTypeTest {

    @Test
    public void shouldParseRFC7231ContentTypeExamples() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "text/html;charset=utf-8",
            "text/html;charset=UTF-8",
            "Text/HTML;Charset=\"utf-8\"",
            "text/html; charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample), is(new MediaType("text", "html").withCharset("utf-8")));
        }
    }

    @Test
    public void shouldParseContentTypeWithSpecialCharacters() {
        assertThat(MediaType.parse("application/my.type+xml"), is(new MediaType("application", "my.type+xml")));
        assertThat(MediaType.parse("application/my.type+xml;charset=utf-8"), is(new MediaType("application", "my.type+xml").withCharset("utf-8")));
        assertThat(MediaType.parse("application/my.type+xml; Charset=\"utf-8\""), is(new MediaType("application", "my.type+xml").withCharset("utf-8")));
    }

    @Test
    public void shouldParseWithoutCharset() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "text/html",
            "text/html;",
            "Text/HTML",
            "Text/HTML;",
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample), is(new MediaType("text", "html")));
        }
    }

    @Test
    public void shouldParseWithoutType() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "charset=utf-8",
            "charset=UTF-8",
            "Charset=\"utf-8\"",
            "charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample), is(new MediaType(null, null).withCharset("utf-8")));
        }
    }

    @Test
    public void shouldSerialiseToStringRFC7231ContentTypeExamples() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "text/html;charset=utf-8",
            "text/html;charset=UTF-8",
            "Text/HTML;Charset=\"utf-8\"",
            "text/html; charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample).toString(), is("text/html; charset=utf-8"));
        }
    }

    @Test
    public void shouldSerialiseToStringWithoutCharset() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "text/html",
            "text/html;",
            "Text/HTML",
            "Text/HTML;",
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample).toString(), is("text/html"));
        }
    }

    @Test
    public void shouldSerialiseToStringWithoutType() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "charset=utf-8",
            "charset=UTF-8",
            "Charset=\"utf-8\"",
            "charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample).toString(), is("charset=utf-8"));
        }
    }

    @Test
    public void shouldSerialiseToStringFromConstructor() {
        assertThat(MediaType.create("text", "html").withCharset(StandardCharsets.UTF_8).toString(), is("text/html; charset=utf-8"));
        assertThat(MediaType.create("text", "html").withCharset("utf-8").toString(), is("text/html; charset=utf-8"));
        assertThat(MediaType.create("text", "html").withCharset("UTF-8").toString(), is("text/html; charset=utf-8"));
        assertThat(MediaType.create(null, null).withCharset(StandardCharsets.UTF_8).toString(), is("charset=utf-8"));
    }

    @Test
    public void shouldSupportSettingCharsetObject() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "text/html;charset=utf-8",
            "text/html;charset=UTF-8",
            "Text/HTML;Charset=\"utf-8\"",
            "text/html; charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample), is(MediaType.create("text", "html").withCharset(StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void shouldSupportSettingCharsetObjectWithoutType() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "charset=utf-8",
            "charset=UTF-8",
            "Charset=\"utf-8\"",
            "charset=\"utf-8\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample), is(MediaType.create(null, null).withCharset(StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void shouldSupportAdditionParameters() {
        assertThat(MediaType.parse("application/soap+xml;charset=UTF-8;action=\"somerandomstuff\""), is(new MediaType("application", "soap+xml", ImmutableMap.of(
            "action", "somerandomstuff"
        )).withCharset(StandardCharsets.UTF_8)));
    }

    @Test
    public void shouldSerialiseToStringWithAdditionParameters() {
        assertThat(new MediaType("application", "soap+xml", ImmutableMap.of(
            "action", "somerandomstuff"
        )).withCharset(StandardCharsets.UTF_8).toString(), is("application/soap+xml; action=somerandomstuff; charset=utf-8"));
    }

    @Test
    public void shouldSerialiseToStringWithAdditionParametersAndNoCharset() {
        assertThat(new MediaType("application", "soap+xml", ImmutableMap.of(
            "action", "somerandomstuff"
        )).toString(), is("application/soap+xml; action=somerandomstuff"));
    }

    @Test
    public void shouldParseContentTypeWithAdditionalParameters() {
        // when
        String[] rfcContentTypeExamples = new String[]{
            "application/soap+xml;charset=UTF-8;action=\"somerandomstuff\"",
            "application/soap+xml;charset=UTF-8;action=somerandomstuff",
            "application/soap+xml; charset=UTF-8; action=\"somerandomstuff\"",
            "charset=UTF-8;action=\"somerandomstuff\""
        };

        for (String rfcContentTypeExample : rfcContentTypeExamples) {
            // then
            assertThat(rfcContentTypeExample, MediaType.parse(rfcContentTypeExample).getCharset(), is(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void shouldReturnDefaultCharset() {
        List<String> binaryContentTypes = Arrays.asList(
            "",
            "application/applixware",
            "application/font-tdpfr",
            "application/java-archive",
            "application/java-serialized-object",
            "application/java-vm",
            "application/mp4",
            "application/octet-stream",
            "application/pdf",
            "application/pkcs10",
            "application/pkix-cert",
            "application/x-font-bdf",
            "application/x-font-ghostscript",
            "application/x-font-linux-psf",
            "application/x-font-otf",
            "application/x-font-pcf",
            "application/x-font-snf",
            "application/x-font-ttf",
            "application/x-font-type1",
            "application/font-woff",
            "application/x-java-jnlp-file",
            "application/x-latex",
            "application/x-shockwave-flash",
            "application/x-silverlight-app",
            "application/x-stuffit",
            "application/x-tar",
            "application/x-tex",
            "application/x-tex-tfm",
            "application/x-x509-ca-cert",
            "application/zip",
            "audio/midi",
            "audio/mp4",
            "audio/mpeg",
            "audio/ogg",
            "audio/x-aiff",
            "audio/x-wav",
            "audio/xm",
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/png",
            "image/sgi",
            "image/tiff",
            "image/x-xbitmap",
            "video/jpeg",
            "video/mp4",
            "video/mpeg",
            "video/ogg",
            "video/quicktime",
            "video/x-msvideo",
            "video/x-sgi-movie"
        );
        for (String contentType : binaryContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat("\"" + contentType + "\" should be default charset CharsetUtil.ISO_8859_1", parse.getCharsetOrDefault(), is(StandardCharsets.ISO_8859_1));
        }
    }

    @Test
    public void shouldNotDetectAsBinaryContentTypeAsJsonOrStringOrXml() {
        List<String> binaryContentTypes = Arrays.asList(
            "application/applixware",
            "application/font-tdpfr",
            "application/java-archive",
            "application/java-serialized-object",
            "application/java-vm",
            "application/mp4",
            "application/octet-stream",
            "application/pdf",
            "application/pkcs10",
            "application/pkix-cert",
            "application/x-font-bdf",
            "application/x-font-ghostscript",
            "application/x-font-linux-psf",
            "application/x-font-otf",
            "application/x-font-pcf",
            "application/x-font-snf",
            "application/x-font-ttf",
            "application/x-font-type1",
            "application/font-woff",
            "application/x-java-jnlp-file",
            "application/x-latex",
            "application/x-shockwave-flash",
            "application/x-silverlight-app",
            "application/x-stuffit",
            "application/x-tar",
            "application/x-tex",
            "application/x-tex-tfm",
            "application/x-x509-ca-cert",
            "application/zip",
            "audio/midi",
            "audio/mp4",
            "audio/mpeg",
            "audio/ogg",
            "audio/x-aiff",
            "audio/x-wav",
            "audio/xm",
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/png",
            "image/sgi",
            "image/tiff",
            "image/x-xbitmap",
            "video/jpeg",
            "video/mp4",
            "video/mpeg",
            "video/ogg",
            "video/quicktime",
            "video/x-msvideo",
            "video/x-sgi-movie"
        );
        for (String contentType : binaryContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat(contentType + " should not be json", parse.isJson(), is(false));
            assertThat(contentType + " should not be xml", parse.isXml(), is(false));
            assertThat(contentType + " should not be string", parse.isString(), is(false));
        }
    }

    @Test
    public void shouldDetectAsJson() {
        List<String> jsonContentTypes = Arrays.asList(
            "application/json",
            "text/json"
        );
        for (String contentType : jsonContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat(contentType + " should be json", parse.isJson(), is(true));
        }
    }

    @Test
    public void shouldNotDetectAsJson() {
        List<String> jsonContentTypes = Arrays.asList(
            "application/x-ndjson"
        );
        for (String contentType : jsonContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat(contentType + " should not be json", parse.isJson(), is(false));
        }
    }

    @Test
    public void shouldDetectAsXml() {
        List<String> xmlContentTypes = Arrays.asList(
            "application/xml",
            "text/xml",
            "application/xhtml+xml",
            "application/vnd.mozilla.xul+xml",
            "application/wspolicy+xml"
        );
        for (String contentType : xmlContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat(contentType + " should be xml", parse.isXml(), is(true));
        }
    }

    @Test
    public void shouldDetectAsString() {
        List<String> xmlContentTypes = Arrays.asList(
            "application/json",
            "application/text",
            "application/html",
            "text/json",
            "text/plain",
            "application/xml",
            "text/xml",
            "application/xhtml+xml",
            "application/vnd.mozilla.xul+xml",
            "application/wspolicy+xml",
            "charset=utf-8",
            "charset=UTF-8",
            "Charset=\"utf-8\"",
            "charset=\"utf-8\"",
            "text/html;charset=utf-8",
            "text/html;charset=UTF-8",
            "Text/HTML;Charset=\"utf-8\"",
            "text/html; charset=\"utf-8\""
        );
        for (String contentType : xmlContentTypes) {
            MediaType parse = MediaType.parse(contentType);
            assertThat(contentType + " should be xml", parse.isString(), is(true));
        }
    }

    @Test
    public void shouldEqual() {
        String[] contentTypeExamples = new String[]{
            "application/soap+xml;charset=UTF-8;action=\"somerandomstuff\"",
            "application/soap+xml;charset=UTF-8;action=somerandomstuff",
            "application/soap+xml; charset=UTF-8; action=\"somerandomstuff\"",
            "charset=UTF-8;action=\"somerandomstuff\"",
            "application/json",
            "application/text",
            "application/html",
            "text/json",
            "text/plain",
            "application/xml",
            "text/xml",
            "application/xhtml+xml",
            "application/vnd.mozilla.xul+xml",
            "application/wspolicy+xml",
            "charset=utf-8",
            "charset=UTF-8",
            "Charset=\"utf-8\"",
            "charset=\"utf-8\"",
            "text/html;charset=utf-8",
            "text/html;charset=UTF-8",
            "Text/HTML;Charset=\"utf-8\"",
            "text/html; charset=\"utf-8\"",
            "application/applixware",
            "application/font-tdpfr",
            "application/java-archive",
            "application/java-serialized-object",
            "application/java-vm",
            "application/mp4",
            "application/octet-stream",
            "application/pdf",
            "application/pkcs10",
            "application/pkix-cert",
            "application/x-font-bdf",
            "application/x-font-ghostscript",
            "application/x-font-linux-psf",
            "application/x-font-otf",
            "application/x-font-pcf",
            "application/x-font-snf",
            "application/x-font-ttf",
            "application/x-font-type1",
            "application/font-woff",
            "application/x-java-jnlp-file",
            "application/x-latex",
            "application/x-shockwave-flash",
            "application/x-silverlight-app",
            "application/x-stuffit",
            "application/x-tar",
            "application/x-tex",
            "application/x-tex-tfm",
            "application/x-x509-ca-cert",
            "application/zip",
            "audio/midi",
            "audio/mp4",
            "audio/mpeg",
            "audio/ogg",
            "audio/x-aiff",
            "audio/x-wav",
            "audio/xm",
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/png",
            "image/sgi",
            "image/tiff",
            "image/x-xbitmap",
            "video/jpeg",
            "video/mp4",
            "video/mpeg",
            "video/ogg",
            "video/quicktime",
            "video/x-msvideo",
            "video/x-sgi-movie"
        };
        for (String contentTypeExample : contentTypeExamples) {
            assertThat(contentTypeExample, MediaType.parse(contentTypeExample), is(MediaType.parse(MediaType.parse(contentTypeExample).toString())));
        }
        assertThat(MediaType.create("text", "plain").withCharset(StandardCharsets.UTF_16), is(MediaType.parse(MediaType.create("text", "plain").withCharset(StandardCharsets.UTF_16).toString())));
    }

}