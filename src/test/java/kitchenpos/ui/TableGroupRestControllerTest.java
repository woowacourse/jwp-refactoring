package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        final Long tableGroupId = 1L;
        final TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
        final TableGroupResponse response = new TableGroupResponse(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTableRequest(10, false), new OrderTableRequest(10, false)));

        when(tableGroupService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/table-groups")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + tableGroupId))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() throws Exception {
        final Long tableGroupId = 1L;

        doNothing().when(tableGroupService).ungroup(any());

        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId))
                .andExpect(status().isNoContent());
    }
}
