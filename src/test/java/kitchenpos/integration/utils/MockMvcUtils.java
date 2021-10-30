package kitchenpos.integration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

@Component
public class MockMvcUtils {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public MockMvcUtils(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public MockMvcRequest request() {
        return new MockMvcRequest(mockMvc, objectMapper);
    }
}
