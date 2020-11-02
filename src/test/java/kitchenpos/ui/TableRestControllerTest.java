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
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
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

    private static final TableResponse EMPTY_TABLE;

    static {
        final Long orderTableId = 1L;
        final int numberOfGuests = 0;
        final boolean empty = true;

        EMPTY_TABLE = TableResponse.of(orderTableId, null, numberOfGuests, empty);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블 추가")
    @Test
    void create() throws Exception {
        String requestBody = "{\n"
            + "  \"numberOfGuests\": " + EMPTY_TABLE.getNumberOfGuests() + ",\n"
            + "  \"empty\": " + EMPTY_TABLE.isEmpty() + "\n"
            + "}";

        given(tableService.create(any(TableCreateRequest.class)))
            .willReturn(EMPTY_TABLE);

        ResultActions resultActions = mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
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

        ResultActions resultActions = mockMvc.perform(get("/api/tables")
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
        TableResponse table = TableResponse
            .of(EMPTY_TABLE.getId(), EMPTY_TABLE.getTableGroupId(), EMPTY_TABLE.getNumberOfGuests(),
                empty);

        String requestBody = "{\n"
            + "  \"empty\": " + table.isEmpty() + "\n"
            + "}";

        given(tableService.changeEmpty(anyLong(), any(TableChangeEmptyRequest.class)))
            .willReturn(table);

        ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + table.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
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
        TableResponse table = TableResponse
            .of(EMPTY_TABLE.getId(), EMPTY_TABLE.getTableGroupId(), numberOfGuests, false);

        String requestBody = "{\n"
            + "  \"numberOfGuests\": " + table.getNumberOfGuests() + "\n"
            + "}";

        given(tableService
            .changeNumberOfGuests(anyLong(), any(TableChangeNumberOfGuestsRequest.class)))
            .willReturn(table);

        ResultActions resultActions = mockMvc
            .perform(put("/api/tables/" + table.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(table.getId().intValue())))
            .andExpect(jsonPath("$.numberOfGuests", is(table.getNumberOfGuests())))
            .andDo(print());
    }
}
