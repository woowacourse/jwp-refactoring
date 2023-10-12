package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.annotation.ControllerTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // TODO: 모든 Controller 테스트에 존재하는 ObjectMapper 개선하기

    @Test
    void 주문_테이블을_저장한다() throws Exception {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        when(tableService.create(any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions result = mockMvc.perform(post("/api/tables")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(orderTable))
        );

        // then
        result.andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/tables/" + orderTable.getId()),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(orderTable))
        );
    }

    @Test
    void 모든_주문_테이블을_조회한다() throws Exception {
        // given
        List<OrderTable> orderTables = List.of(new OrderTable(), new OrderTable());
        when(tableService.list()).thenReturn(orderTables);

        // when
        ResultActions result = mockMvc.perform(get("/api/tables"));

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(orderTables))
        );
    }

    @Test
    void 테이블의_비어있는_상태를_변경한다() throws Exception {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setTableGroupId(10L);
        orderTable.setEmpty(true);
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
        );

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(orderTable))
        );
    }

    @Test
    void 테이블의_손님_수를_변경한다() throws Exception {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setTableGroupId(10L);
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
        );

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(orderTable))
        );
    }
}
