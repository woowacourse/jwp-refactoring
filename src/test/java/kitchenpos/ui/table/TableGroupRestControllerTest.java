package kitchenpos.ui.table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.table.dto.CreateTableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.ui.ControllerTest;
import kitchenpos.ui.table.dto.CreateTableGroupRequest;
import kitchenpos.ui.table.dto.OrderTableIdRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends ControllerTest {

    @Test
    void 테이블_그룹_생성() throws Exception {
        // given
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(
                new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)
        ));
        String request = objectMapper.writeValueAsString(createTableGroupRequest);

        TableGroup tableGroup = 테이블_그룹(1L);
        OrderTable orderTable = new OrderTable(1L, tableGroup, 0, true);
        CreateTableGroupResponse createTableGroupResponse =
                CreateTableGroupResponse.from(tableGroup, List.of(orderTable));
        given(tableGroupService.create(any())).willReturn(createTableGroupResponse);
        String response = objectMapper.writeValueAsString(createTableGroupResponse);

        // when & then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 테이블_그룹_제거() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(any());

        // when & then
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}
