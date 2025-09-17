package co.com.pragma.dynamodb.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JacksonJsonConverterTest {

    @Test
    void testToJson_Success() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonJsonConverter converter = new JacksonJsonConverter(objectMapper);
        TestObject obj = new TestObject("aaa", 123);

        Optional<String> json = converter.toJson(obj);

        assertTrue(json.isPresent());
        assertTrue(json.get().contains("\"name\":\"aaa\""));
        assertTrue(json.get().contains("\"value\":123"));
    }

    @Test
    void testToJson_Failure() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonJsonConverter converter = new JacksonJsonConverter(objectMapper);

        Optional<String> json = converter.toJson(new Object() {
            private final Object self = this;
        });

        assertFalse(json.isPresent());
    }

    static class TestObject {
        private String name;
        private int value;

        public TestObject() {}

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}