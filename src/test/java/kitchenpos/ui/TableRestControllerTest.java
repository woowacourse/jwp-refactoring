package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.TableFixture.createOrderTable;
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
        OrderTable orderTable = createOrderTable();
        OrderTable savedOrderTable = createOrderTable(orderTableId);

        when(tableService.create(any())).thenReturn(savedOrderTable);

        mockMvc.perform(post("/api/tables")
                .content(objectMapper.writeValueAsString(orderTable))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + orderTableId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedOrderTable)));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(1L), createOrderTable(2L));
        when(tableService.list()).thenReturn(orderTables);

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTables)));
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() throws Exception {
        Long orderTableId = 1L;
        OrderTable orderTable = createOrderTable(orderTableId);
        orderTable.setEmpty(true);

        when(tableService.changeEmpty(any(), any())).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .content(objectMapper.writeValueAsString(orderTable))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = createOrderTable(1L);
        orderTable.setNumberOfGuests(5);

        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/1/number-of-guests")
                .content(objectMapper.writeValueAsString(orderTable))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
    }
}
