package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.TableGroupFixtures;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.request.TableGroupIdRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class TableGroupRestControllerTest extends ControllerTest {

    private TableGroupService tableGroupService;

    @Autowired
    public TableGroupRestControllerTest(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @Test
    void create() throws Exception {
        // given
        given(tableGroupService.create(any())).willReturn(TableGroupFixtures.createTableGroupResponse());

        // when
        List<TableGroupIdRequest> orderTableIds = List.of(new TableGroupIdRequest(1L), new TableGroupIdRequest(2L));
        ResultActions actions = mockMvc.perform(post("/api/table-groups")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new TableGroupCreateRequest(orderTableIds)))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void ungroup() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(any());

        // when
        ResultActions actions = mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L));

        // then
        actions.andExpect(status().isNoContent());
    }
}
