package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("TableGroup을 생성한다.")
    void create() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableRequest(1L), new OrderTableRequest(2L)));

        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.now(),
                List.of(new OrderTableResponse(1L, 1L, 10, false), new OrderTableResponse(1L, 1L, 10, false)));

        given(tableGroupService.create(any(TableGroupCommand.class))).willReturn(tableGroupResponse);

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableGroupRequest)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/table-groups/1"));
    }

    @Test
    @DisplayName("TableGroup을 해제한다.")
    void ungroup() throws Exception {
        doNothing().when(tableGroupService).ungroup(anyLong());
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}
