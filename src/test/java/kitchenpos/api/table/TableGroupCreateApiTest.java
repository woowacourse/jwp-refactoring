package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.OrderTableInTableGroupResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupCreateApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 그룹 생성 API 테스트")
    @Test
    void createTableGroup() throws Exception {
        // given
        final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(1L, 2L));

        // when
        final List<OrderTableInTableGroupResponse> orderTableInTableGroupRespons = List.of(
                new OrderTableInTableGroupResponse(1L, 0, true),
                new OrderTableInTableGroupResponse(2L, 0, true)
        );
        final TableGroupResponse response = new TableGroupResponse(1L, LocalDateTime.now(), orderTableInTableGroupRespons);
        when(tableGroupService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/table-groups/%d", response.getId())));
    }
}
