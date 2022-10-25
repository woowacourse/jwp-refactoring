package kitchenpos.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

class TableGroupRestControllerTest extends ControllerTest {

    @DisplayName("단체 테이블을 지정한다.")
    @Test
    void create() throws Exception {
        // given
        TableGroupResponse tableGroupResponse = new TableGroupResponse(
            1L,
            LocalDateTime.now(),
            List.of(
                new OrderTableResponse(1L, 1L, 2, false),
                new OrderTableResponse(2L, 1L, 2, false))
        );

        given(tableGroupService.create(any(TableGroupRequest.class)))
            .willReturn(tableGroupResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(
                new TableGroupRequest(List.of(1L, 2L)))
            ));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/table-groups/1"))
            .andExpect(content().json(toJson(tableGroupResponse)));
    }

    @DisplayName("단체 테이블을 해제한다.")
    @Test
    void ungroup() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/api/table-groups/1"));

        // then
        assertAll(
            () -> result.andExpect(status().isNoContent()),
            () -> verify(tableGroupService).ungroup(1L)
        );
    }
}
