package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TableListApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 전체 조회 API 테스트")
    @Test
    void listTable() throws Exception {
        // when
        // FIXME: domain -> dto 로 변경
        final OrderTable expectedOrderTable = new OrderTable();
        expectedOrderTable.setId(1L);
        expectedOrderTable.setEmpty(false);
        expectedOrderTable.setNumberOfGuests(1);
        expectedOrderTable.setTableGroupId(1L);
        when(tableService.list()).thenReturn(List.of(expectedOrderTable));

        // then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(expectedOrderTable.getId().intValue())))
                .andExpect(jsonPath("$[0].tableGroupId", is(expectedOrderTable.getTableGroupId().intValue())))
                .andExpect(jsonPath("$[0].numberOfGuests", is(expectedOrderTable.getNumberOfGuests())))
                .andExpect(jsonPath("$[0].empty", is(expectedOrderTable.isEmpty())));
    }
}
