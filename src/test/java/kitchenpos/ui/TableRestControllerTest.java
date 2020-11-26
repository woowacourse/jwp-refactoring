package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
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
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends KitchenPosControllerTest {

    private static final TableResponse EMPTY_TABLE;

    static {
        final Long orderTableId = 1L;
        final int numberOfGuests = 0;
        final boolean empty = true;

        EMPTY_TABLE = TableResponse.of(orderTableId, null, numberOfGuests, empty);
    }

    @MockBean
    private TableService tableService;

    @DisplayName("테이블 추가")
    @Test
    void create() throws Exception {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            EMPTY_TABLE.getNumberOfGuests(), EMPTY_TABLE.isEmpty());

        given(tableService.create(tableCreateRequest))
            .willReturn(EMPTY_TABLE);

        final ResultActions resultActions = mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(tableCreateRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(EMPTY_TABLE.getId().intValue())))
            .andExpect(jsonPath("$.numberOfGuests", is(EMPTY_TABLE.getNumberOfGuests())))
            .andExpect(jsonPath("$.empty", is(EMPTY_TABLE.isEmpty())))
            .andDo(print());
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() throws Exception {
        given(tableService.list())
            .willReturn(Collections.singletonList(EMPTY_TABLE));

        final ResultActions resultActions = mockMvc.perform(get("/api/tables")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(EMPTY_TABLE.getId().intValue())))
            .andDo(print());
    }

    @DisplayName("테이블 상태 변경")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) throws Exception {
        Long tableId = EMPTY_TABLE.getId();
        TableChangeEmptyRequest tableChangeEmptyRequest = new TableChangeEmptyRequest(empty);

        TableResponse table = TableResponse
            .of(tableId, EMPTY_TABLE.getTableGroupId(), EMPTY_TABLE.getNumberOfGuests(),
                tableChangeEmptyRequest.isEmpty());

        given(tableService.changeEmpty(tableId, tableChangeEmptyRequest))
            .willReturn(table);

        final ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + tableId + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(tableChangeEmptyRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(table.getId().intValue())))
            .andExpect(jsonPath("$.empty", is(table.isEmpty())))
            .andDo(print());
    }

    @DisplayName("테이블 손님 수 변경")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void changeNumberOfGuests(int numberOfGuests) throws Exception {
        Long tableId = EMPTY_TABLE.getId();
        TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest
            = new TableChangeNumberOfGuestsRequest(numberOfGuests);

        TableResponse table = TableResponse.of(tableId, EMPTY_TABLE.getTableGroupId(),
            tableChangeNumberOfGuestsRequest.getNumberOfGuests(), false);

        given(tableService.changeNumberOfGuests(tableId, tableChangeNumberOfGuestsRequest))
            .willReturn(table);

        final ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + table.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(tableChangeNumberOfGuestsRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(table.getId().intValue())))
            .andExpect(jsonPath("$.numberOfGuests", is(table.getNumberOfGuests())))
            .andDo(print());
    }
}
