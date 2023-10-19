package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableCreateApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 생성 API 테스트")
    @Test
    void createTable() throws Exception {
        // given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);

        // when
        final OrderTableResponse response = new OrderTableResponse(1L, request.getNumberOfGuests(), request.isEmpty(), null);
        when(tableService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/tables")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/tables/%d", response.getId())));
    }
}
