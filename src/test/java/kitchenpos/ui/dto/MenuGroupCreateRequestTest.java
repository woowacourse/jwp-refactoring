package kitchenpos.ui.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class MenuGroupCreateRequestTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("메뉴 그룹 생성 요청이 정상적으로 dto에 매핑되는지 확인한다.")
    @Test
    void menuGroupCreateRequestTest() {
        final String requestBody = "{\n"
            + "\"name\": \"추천메뉴\"\n"
            + "}";
        assertDoesNotThrow(() -> objectMapper.readValue(requestBody,
            MenuGroupCreateRequest.class));
    }
}
