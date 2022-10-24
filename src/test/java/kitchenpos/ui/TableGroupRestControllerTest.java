package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
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
        long id = 1L;
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of());
        tableGroup.setId(id);

        given(tableGroupService.create(any())).willReturn(tableGroup);

        // when
        ResultActions actions = mockMvc.perform(post("/api/table-groups")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(tableGroup))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + id));
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
