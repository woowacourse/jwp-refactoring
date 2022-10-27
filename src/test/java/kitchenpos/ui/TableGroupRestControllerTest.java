package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.SavedOrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;

@ApiTest
@DisplayName("TableGroup API 테스트")
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() throws Exception {
        final SavedOrderTableRequest orderTableRequest1 = new SavedOrderTableRequest(1L, 1, false);
        final SavedOrderTableRequest orderTableRequest2 = new SavedOrderTableRequest(2L, 1, false);
        final TableGroupRequest request = new TableGroupRequest(List.of(orderTableRequest1, orderTableRequest2));
        final String body = objectMapper.writeValueAsString(request);

        final long tableGroupId = 1L;
        final OrderTableResponse orderTableResponse1 = new OrderTableResponse(1L, tableGroupId, 1, false);
        final OrderTableResponse orderTableResponse2 = new OrderTableResponse(2L, tableGroupId, 1, false);
        final TableGroupResponse response = new TableGroupResponse(tableGroupId, LocalDateTime.now(), List.of(orderTableResponse1, orderTableResponse2));
        BDDMockito.given(tableGroupService.create(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/table-groups")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/table-groups/" + response.getId()))
        ;
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/" + 1L))
            .andExpect(status().isNoContent())
        ;
    }
}
