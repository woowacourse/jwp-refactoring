package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.OrderTableFixtures;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
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
        OrderTableResponse response = OrderTableFixtures.createOrderTableResponse();
        given(tableService.create(any())).willReturn(response);

        // when
        OrderTableCreateRequest request = new OrderTableCreateRequest(response.getNumberOfGuests(), response.isEmpty());
        ResultActions actions = mockMvc.perform(post("/api/tables")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + response.getId()));
    }

    @Test
    void list() throws Exception {
        // given
        OrderTableResponse response = OrderTableFixtures.createOrderTableResponse();
        given(tableService.list()).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/tables"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTableResponse response = OrderTableFixtures.createOrderTableResponse();
        given(tableService.changeEmpty(any(), anyBoolean())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(patch("/api/tables/{orderTableId}", 1L)
                .queryParam("empty", String.valueOf(true))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTableResponse response = OrderTableFixtures.createOrderTableResponse();
        given(tableService.changeNumberOfGuests(any(), anyInt())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(patch("/api/tables/{orderTableId}", 1L)
                .queryParam("numberOfGuests", String.valueOf(2))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
