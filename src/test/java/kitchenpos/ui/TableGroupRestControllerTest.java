package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends KitchenPosControllerTest {

    private static final TableGroupResponse TABLE_GROUP;

    static {
        final long tableGroupId = 1L;
        final long orderTablesFirstId = 2L;
        final long orderTablesSecondId = 3L;

        List<TableResponse> orderTables = Arrays.asList(
            TableResponse.of(orderTablesFirstId, tableGroupId, 10, false),
            TableResponse.of(orderTablesSecondId, tableGroupId, 10, false)
        );
        TABLE_GROUP = TableGroupResponse.of(tableGroupId, LocalDateTime.now(), orderTables);
    }

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
            TABLE_GROUP.getOrderTables().get(0).getId(),
            TABLE_GROUP.getOrderTables().get(1).getId())
        );

        given(tableGroupService.create(tableGroupRequest))
            .willReturn(TABLE_GROUP);

        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(tableGroupRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(TABLE_GROUP.getId().intValue())))
            .andExpect(jsonPath("$.orderTables", hasSize(2)))
            .andExpect(jsonPath("$.orderTables[0].id",
                is(TABLE_GROUP.getOrderTables().get(0).getId().intValue())))
            .andExpect(jsonPath("$.orderTables[1].id",
                is(TABLE_GROUP.getOrderTables().get(1).getId().intValue())))
            .andDo(print());
    }

    @DisplayName("테이블 그룹 제거")
    @Test
    void ungroup() throws Exception {
        final ResultActions resultActions = mockMvc
            .perform(delete("/api/table-groups/" + TABLE_GROUP.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}
