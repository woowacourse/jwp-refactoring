package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableCreateCommand;
import kitchenpos.application.dto.request.OrderTableEmptyCommand;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableEmptyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class TableRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("OrderTable을 생성한다.")
    void create() throws Exception {
        OrderTableCreateCommand orderTableCreateCommand = new OrderTableCreateCommand(10, false);
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 10, false);

        given(tableService.create(any(OrderTableCreateCommand.class))).willReturn(orderTableResponse);
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTableCreateCommand)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/tables/1"));
    }

    @Test
    @DisplayName("OrderTable List를 반환한다.")
    void list() throws Exception {
        OrderTableResponse orderTable1 = new OrderTableResponse(1L, 1L, 10, false);
        OrderTableResponse orderTable2 = new OrderTableResponse(2L, 2L, 5, false);

        List<OrderTableResponse> orderTables = List.of(orderTable1, orderTable2);
        given(tableService.list()).willReturn(orderTables);
        mockMvc.perform(get("/api/tables"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orderTables)));
    }

    @Test
    @DisplayName("비어있는 상태로 변경한다.")
    void changeEmpty() throws Exception {
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 10, true);
        given(tableService.changeEmpty(anyLong(), any(OrderTableEmptyCommand.class))).willReturn(orderTableResponse);

        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderTableEmptyRequest(true))))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 10, true);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(orderTable);

        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orderTable)));
    }
}
