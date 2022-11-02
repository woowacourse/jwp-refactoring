package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class OrderTableGroupRestControllerTest extends ControllerTest {

    @DisplayName("POST /api/table-groups")
    @Test
    void create() throws Exception {
        // given
        TableGroupRequest request = new TableGroupRequest(
                List.of(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L)
                )
        );
        TableGroupResponse response = new TableGroupResponse(1L, LocalDateTime.now(),
                List.of(
                        new OrderTableResponse(1L, 1L, 2, false)
                )
        );
        given(tableGroupApiService.create(any(TableGroupRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + response.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }

    @DisplayName("DELETE /api/table-groups/{tableGroupId}")
    @Test
    void ungroup() throws Exception {
        // given
        doNothing().when(tableGroupApiService).ungroup(any(Long.class));

        // when
        ResultActions result = mockMvc.perform(delete("/api/table-groups/1"));

        // then
        result.andExpect(status().isNoContent());
    }
}
