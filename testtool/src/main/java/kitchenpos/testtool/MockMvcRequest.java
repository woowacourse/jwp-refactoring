package kitchenpos.testtool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcRequest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private MockHttpServletRequestBuilder builder;

    public MockMvcRequest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public MockMvcRequest get(String url, Object ... pathVariables) {
        this.builder = MockMvcRequestBuilders.get(url, pathVariables);
        return this;
    }

    public MockMvcRequest post(String url, Object ... pathVariables) {
        this.builder = MockMvcRequestBuilders.post(url, pathVariables);
        return this;
    }

    public MockMvcRequest put(String url, Object ... pathVariables) {
        this.builder = MockMvcRequestBuilders.put(url, pathVariables);
        return this;
    }

    public MockMvcRequest delete(String url, Object ... pathVariables) {
        this.builder = MockMvcRequestBuilders.delete(url, pathVariables);
        return this;
    }

    public MockMvcRequest content(Object content) {
        try {
            this.builder = builder.content(objectMapper.writeValueAsString(content))
                .contentType(MediaType.APPLICATION_JSON);
            return this;
        } catch (Exception e) {
            throw new IllegalStateException("wrong contents");
        }
    }

    public <T> MockMvcResponse<T> asSingleResult(Class<T> returnType) {
        return executeRequest(json -> {
            try {
                if(json == null || json.isEmpty()) return null;
                return objectMapper.readValue(json, returnType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public <T> MockMvcResponse<List<T>> asMultiResult(Class<T> returnType) {
        return executeRequest(json -> {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, returnType));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private <T> MockMvcResponse<T> executeRequest(Function<String, T> function) {
        try {
            final MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();;

            final T result = function
                .apply(response.getContentAsString(StandardCharsets.UTF_8));
            final int status = response.getStatus();

            return new MockMvcResponse<>(result, HttpStatus.resolve(status));
        } catch (Exception exception) {
            exception.printStackTrace();
            return new MockMvcResponse<>(null, HttpStatus.BAD_REQUEST, exception.getCause().getMessage());
        }
    }
}
