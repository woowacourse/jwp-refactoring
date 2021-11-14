package kitchenpos.order.ui;

import kitchenpos.support.RestControllerTest;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static kitchenpos.order.fixture.TableGroupFixture.createTableGroupRequest;
import static kitchenpos.order.fixture.TableGroupFixture.createTableGroupResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends RestControllerTest {

    @MockBean
    private TableGroupService mockTableGroupService;

    @DisplayName("테이블 그룹 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(1L, 2L);
        TableGroupResponse tableGroupResponse = createTableGroupResponse(1L, tableGroupRequest);
        when(mockTableGroupService.create(any())).thenReturn(tableGroupResponse);
        mockMvc.perform(post("/api/table-groups")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableGroupRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + tableGroupResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(tableGroupResponse)));
    }

    @DisplayName("테이블 그룹 해제 요청을 처리한다.")
    @Test
    void ungroup() throws Exception {
        doNothing().when(mockTableGroupService).ungroup(1L);
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
                .andExpect(status().isNoContent());
    }
}
