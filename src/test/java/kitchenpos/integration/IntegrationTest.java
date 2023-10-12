package kitchenpos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestConstructor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T jsonToClass(final String responseBody, final TypeReference<T> typeReference)
            throws JsonProcessingException {
        return objectMapper.readValue(responseBody, typeReference);
    }
}
