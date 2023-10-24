package kitchenpos.ui;

import static java.time.LocalDateTime.now;
import static kitchenpos.util.ObjectCreator.getObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.table.service.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.CreateTableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create()
            throws Exception {
        // given
        final List<OrderTable> orderTables = List.of();

        final CreateTableGroupRequest request = getObject(CreateTableGroupRequest.class, 1L, orderTables);

        final TableGroup tableGroup = new TableGroup(1L, now());

        when(tableGroupService.create(any()))
                .thenReturn(TableGroupResponse.from(tableGroup,orderTables));

        // when & then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("createdDate").isString())
                .andExpect(jsonPath("orderTables").isArray());
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
                .andExpect(status().isNoContent());
    }
}
