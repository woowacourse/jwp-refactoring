package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.TableGroupService;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends RestControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 지정 요청을 수행한다.")
    @Test
    void create() throws Exception {
        TableGroupCreateRequest tableGroupCreateRequest = TableGroupCreateRequest.of(Arrays.asList(1L, 2L));

        TableResponse tableResponse1 = new TableResponse(1L, 1L, 3, false);
        TableResponse tableResponse2 = new TableResponse(2L, 1L, 4, false);
        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.now(),
            Arrays.asList(tableResponse1, tableResponse2));

        given(tableGroupService.create(any(TableGroupCreateRequest.class))).willReturn(tableGroupResponse);

        mockMvc.perform(post("/api/table-groups")
            .content(objectMapper.writeValueAsString(tableGroupCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/table-groups/" + tableGroupResponse.getId()))
            .andDo(print());
    }

    @DisplayName("테이블 그룹 해제 요청을 수행한다.")
    @Test
    void ungroup() throws Exception {
        doNothing().when(tableGroupService).ungroup(anyLong());

        mockMvc.perform(delete("/api/table-groups/{tableId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}