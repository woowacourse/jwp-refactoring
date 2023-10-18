package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableRestControllerTest extends ControllerTest {

    @Test
    void 주문_테이블_생성() throws Exception {
        // given
        OrderTable orderTable = 주문_테이블();
        String request = objectMapper.writeValueAsString(orderTable);
        OrderTable savedOrderTable = 주문_테이블(1L);
        given(tableService.create(any())).willReturn(savedOrderTable);
        String response = objectMapper.writeValueAsString(savedOrderTable);

        // when & then
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_조회() throws Exception {
        // given
        OrderTable orderTable1 = 주문_테이블(1L);
        OrderTable orderTable2 = 주문_테이블(2L);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        given(tableService.list()).willReturn(orderTables);
        String response = objectMapper.writeValueAsString(orderTables);

        // when & then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_상태_변경() throws Exception {
        // given
        OrderTable orderTable1 = 주문_테이블();
        String request = objectMapper.writeValueAsString(orderTable1);
        OrderTable savedOrderTable1 = 주문_테이블(1L);
        given(tableService.changeEmpty(any())).willReturn(savedOrderTable1);
        String response = objectMapper.writeValueAsString(savedOrderTable1);

        // when & then
        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_인원_변경() throws Exception {
        // given
        OrderTable orderTable1 = 주문_테이블();
        String request = objectMapper.writeValueAsString(orderTable1);
        OrderTable savedOrderTable1 = 주문_테이블(1L);
        given(tableService.changeNumberOfGuests(any())).willReturn(savedOrderTable1);
        String response = objectMapper.writeValueAsString(savedOrderTable1);

        // when & then
        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
