package kitchenpos.ui;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.createAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
public class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @DisplayName("Order Table을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final OrderTable orderTable = ORDER_TABLE1.create();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getNumberOfGuests(),
                orderTable.isEmpty());

        given(tableService.create(any(OrderTableRequest.class))).willReturn(orderTable);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("OrderTable들을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final List<OrderTable> orderTables = createAll();

        given(tableService.list()).willReturn(orderTables);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/tables")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }
}
