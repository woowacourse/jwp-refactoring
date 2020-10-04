package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    private static final long ORDER_TABLE_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 0;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블 추가")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ORDER_TABLE_ID);
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS);
        orderTable.setEmpty(true);

        String requestBody = "{\n"
            + "  \"numberOfGuests\": " + orderTable.getNumberOfGuests() + ",\n"
            + "  \"empty\": " + orderTable.isEmpty() + "\n"
            + "}";

        given(tableService.create(any(OrderTable.class)))
            .willReturn(orderTable);

        ResultActions resultActions = mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(orderTable.getId().intValue())))
            .andExpect(jsonPath("$.numberOfGuests", is(orderTable.getNumberOfGuests())))
            .andExpect(jsonPath("$.empty", is(orderTable.isEmpty())))
            .andDo(print());
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ORDER_TABLE_ID);

        given(tableService.list())
            .willReturn(Collections.singletonList(orderTable));

        ResultActions resultActions = mockMvc.perform(get("/api/tables")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(orderTable.getId().intValue())))
            .andDo(print());
    }

    @DisplayName("테이블 상태 변경")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ORDER_TABLE_ID);
        orderTable.setEmpty(empty);

        String requestBody = "{\n"
            + "  \"empty\": " + orderTable.isEmpty() + "\n"
            + "}";

        given(tableService.changeEmpty(anyLong(), any(OrderTable.class)))
            .willReturn(orderTable);

        ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + orderTable.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(orderTable.getId().intValue())))
            .andExpect(jsonPath("$.empty", is(orderTable.isEmpty())))
            .andDo(print());
    }

    @DisplayName("테이블 손님 수 변경")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ORDER_TABLE_ID);
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS);

        String requestBody = "{\n"
            + "  \"numberOfGuests\": " + orderTable.getNumberOfGuests() + "\n"
            + "}";

        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class)))
            .willReturn(orderTable);

        ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(orderTable.getId().intValue())))
            .andExpect(jsonPath("$.numberOfGuests", is(orderTable.getNumberOfGuests())))
            .andDo(print());
    }
}
