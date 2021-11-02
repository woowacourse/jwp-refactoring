package kitchenpos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectMapperForTest {

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    protected String objectToJson(final Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
