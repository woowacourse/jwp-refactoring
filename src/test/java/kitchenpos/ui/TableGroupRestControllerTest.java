package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static kitchenpos.TableFixture.createTableGroup;
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
        Long tableGroupId = 1L;
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = createTableGroup(tableGroupId);

        when(tableGroupService.create(any())).thenReturn(savedTableGroup);

        mockMvc.perform(post("/api/table-groups")
                .content(objectMapper.writeValueAsString(tableGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + tableGroupId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedTableGroup)));
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() throws Exception {
        Long tableGroupId = 1L;
        TableGroup tableGroup = createTableGroup(tableGroupId);

        doNothing().when(tableGroupService).ungroup(any());

        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .content(objectMapper.writeValueAsString(tableGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}