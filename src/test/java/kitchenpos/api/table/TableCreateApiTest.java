package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
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
        final String request = "{\n" +
                "  \"numberOfGuests\": 0,\n" +
                "  \"empty\": true\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final OrderTable expectedOrderTable = new OrderTable();
        expectedOrderTable.setId(expectedId);
        when(tableService.create(any(OrderTable.class))).thenReturn(expectedOrderTable);

        // then
        mockMvc.perform(post("/api/tables")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/tables/%d", expectedId)));
    }
}
