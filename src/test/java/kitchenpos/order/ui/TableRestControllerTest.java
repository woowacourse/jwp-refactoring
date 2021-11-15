package kitchenpos.order.ui;

import kitchenpos.order.application.TableService;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.support.RestControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.order.fixture.OrderTableFixture.createOrderTableRequest;
import static kitchenpos.order.fixture.OrderTableFixture.createOrderTableResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends RestControllerTest {

    @MockBean
    private TableService mockTableService;

    @DisplayName("테이블 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        OrderTableRequest orderTableRequest = createOrderTableRequest();
        OrderTableResponse orderTableResponse = createOrderTableResponse(1L, orderTableRequest);
        when(mockTableService.create(any())).thenReturn(orderTableResponse);
        mockMvc.perform(post("/api/tables")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTableRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + orderTableResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @DisplayName("테이블 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<OrderTableResponse> expected = Collections.singletonList(createOrderTableResponse());
        when(mockTableService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @DisplayName("테이블 empty 상태 변경 요청을 처리한다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTableRequest orderTableRequest = createOrderTableRequest();
        OrderTableResponse orderTableResponse = createOrderTableResponse(1L, orderTableRequest);
        ;
        when(mockTableService.changeEmpty(any(), any())).thenReturn(orderTableResponse);
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTableRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @DisplayName("테이블 손님 수 변경 요청을 처리한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTableRequest orderTableRequest = createOrderTableRequest();
        OrderTableResponse orderTableResponse = createOrderTableResponse(1L, orderTableRequest);
        when(mockTableService.changeNumberOfGuests(any(), any())).thenReturn(orderTableResponse);
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTableRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }
}
