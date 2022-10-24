package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class TableRestControllerTest extends ControllerTest {

    private TableService tableService;

    @Autowired
    public TableRestControllerTest(TableService tableService) {
        this.tableService = tableService;
    }

    @Test
    void create() throws Exception {
        // given
        long id = 1L;
        boolean empty = true;
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable(id, null, numberOfGuests, empty);

        given(tableService.create(any())).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(post("/api/tables")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new OrderTableCreateRequest(numberOfGuests, empty)))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + id));
    }

    @Test
    void list() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        given(tableService.list()).willReturn(List.of(orderTable));

        // when
        ResultActions actions = mockMvc.perform(get("/api/tables"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(orderTable))));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        given(tableService.changeEmpty(any(), any())).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTable))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1L, 2, false);
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTable))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
    }
}
