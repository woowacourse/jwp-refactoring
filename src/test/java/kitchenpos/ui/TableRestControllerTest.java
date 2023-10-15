package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableNumberOfGuestRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixtrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Test
    void 테이블을_생성한다() throws Exception {
        // given
        OrderTable createdTable = OrderTableFixtrue.orderTable(10, false);
        OrderTableRequest request = new OrderTableRequest(10, false);

        // when
        when(tableService.create(any(OrderTableRequest.class))).thenReturn(createdTable);

        // then
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + createdTable.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdTable)));
    }

    @Test
    void 테이블을_전체_조회한다() throws Exception {
        // given
        OrderTable table1 = OrderTableFixtrue.orderTable(10, false);
        OrderTable table2 = OrderTableFixtrue.orderTable(12, false);

        // when
        when(tableService.list()).thenReturn(List.of(table1, table2));

        // then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(table1, table2))));
    }

    @Test
    void 빈_테이블로_변경한다() throws Exception {
        // given
        Long tableId = 1L;
        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);
        OrderTable orderTable = OrderTableFixtrue.orderTable(10, true);

        // when
        when(tableService.changeEmpty(tableId, orderTableEmptyRequest)).thenReturn(orderTable);

        // then
        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderTableEmptyRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void 테이블의_손님_수를_변경한다() throws Exception {
        // given
        Long tableId = 1L;
        OrderTable updatedTable = OrderTableFixtrue.orderTable(10, false);
        OrderTableNumberOfGuestRequest request = new OrderTableNumberOfGuestRequest(10);

        // when
        when(tableService.changeNumberOfGuests(tableId, request)).thenReturn(updatedTable);

        // then
        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updatedTable)))
                .andExpect(status().isOk());
    }
}

