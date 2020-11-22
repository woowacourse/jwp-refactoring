package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.TableService;
import kitchenpos.dto.NumberOfGuestsChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyChangeRequest;
import kitchenpos.dto.TableResponse;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends RestControllerTest {
    @MockBean
    private TableService tableService;

    @DisplayName("테이블 생성 요청을 수행한다.")
    @Test
    void create() throws Exception {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(2, true);
        TableResponse tableResponse = new TableResponse(1L, null, tableCreateRequest.getNumberOfGuests(),
            tableCreateRequest.isEmpty());

        given(tableService.create(any(TableCreateRequest.class))).willReturn(tableResponse);

        mockMvc.perform(post("/api/tables")
            .content(objectMapper.writeValueAsString(tableCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/tables/" + tableResponse.getId()))
            .andDo(print());
    }

    @DisplayName("테이블 전체 조회 요청을 수행한다.")
    @Test
    void list() throws Exception {
        TableResponse tableResponse = new TableResponse(1L, null, 4,
            true);

        given(tableService.list()).willReturn(Collections.singletonList(tableResponse));

        mockMvc.perform(get("/api/tables")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("테이블의 상태 변경 요청을 수행한다.")
    @Test
    void changeEmpty() throws Exception {
        Long tableId = 1L;
        TableEmptyChangeRequest tableEmptyChangeRequest = new TableEmptyChangeRequest(false);
        TableResponse tableResponse = new TableResponse(tableId, null, 1,
            tableEmptyChangeRequest.isEmpty());

        given(tableService.changeEmpty(anyLong(), any(TableEmptyChangeRequest.class))).willReturn(tableResponse);

        mockMvc.perform(put("/api/tables/{tableId}/empty", tableId)
            .content(objectMapper.writeValueAsString(tableEmptyChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("테이블의 손님 수 변경 요청을 수행한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        Long tableId = 1L;
        NumberOfGuestsChangeRequest numberOfGuestsChangeRequest = new NumberOfGuestsChangeRequest(2);
        TableResponse tableResponse = new TableResponse(tableId, null, numberOfGuestsChangeRequest.getNumberOfGuests(),
            false);

        given(tableService.changeNumberOfGuests(anyLong(), any(NumberOfGuestsChangeRequest.class))).willReturn(
            tableResponse);

        mockMvc.perform(put("/api/tables/{tableId}/number-of-guests", tableId)
            .content(objectMapper.writeValueAsString(numberOfGuestsChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}