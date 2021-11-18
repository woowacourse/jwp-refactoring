package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.ui.dto.OrderTableEmptyRequest;
import kitchenpos.ui.dto.OrderTableGuestRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {

    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Long orderTableId = 1L;
        OrderTableRequest request = new OrderTableRequest(10, true);
        OrderTableResponse response = new OrderTableResponse(orderTableId, 10, true);

        when(tableService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/tables")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + orderTableId))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<OrderTableResponse> response = Collections.singletonList(new OrderTableResponse(1L, 10, true));

        when(tableService.list()).thenReturn(response);

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() throws Exception {
        Long orderTableId = 1L;
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);
        OrderTableResponse response = new OrderTableResponse(orderTableId, 10, true);

        when(tableService.changeEmpty(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        Long orderTableId = 1L;
        OrderTableGuestRequest request = new OrderTableGuestRequest(20);
        OrderTableResponse response = new OrderTableResponse(orderTableId, 20, true);

        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
