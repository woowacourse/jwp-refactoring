package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.ControllerTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {

    private final String defaultTableUrl = "/api/tables";

    @MockBean
    private TableService tableService;

    @Test
    void table을_생성할_수_있다() throws Exception {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        when(tableService.create(any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions response = postRequestWithJson(defaultTableUrl, orderTable);

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", defaultTableUrl + "/" + 1))
                .andExpect(content().string(objectMapper.writeValueAsString(orderTable)));
    }

    @Test
    void table_목록을_조회할_수_있다() throws Exception {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        when(tableService.list()).thenReturn(Arrays.asList(orderTable));

        // when
        ResultActions response = getRequest(defaultTableUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(Arrays.asList(orderTable))));
    }

    @Test
    void table을_비울_수_있다() throws Exception {
        // given
        String url = "/api/tables/1/empty";
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        when(tableService.changeEmpty(any(Long.class), any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions response = putRequestWithJson(url, orderTable);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTable)));
    }

    @Test
    void table_손님의_수를_변경할_수_있다() throws Exception {
        // given
        String url = "/api/tables/1/number-of-guests";
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(3);
        when(tableService.changeNumberOfGuests(any(Long.class), any(OrderTable.class))).thenReturn(orderTable);

        // when
        ResultActions response = putRequestWithJson(url, orderTable);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTable)));
    }
}
