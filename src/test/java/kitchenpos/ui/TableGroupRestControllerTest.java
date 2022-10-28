package kitchenpos.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.mapper.TableGroupDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends RestControllerTest {

    @Autowired
    private TableGroupDtoMapper tableGroupDtoMapper;

    @Test
    void 단체_지정에_성공한다() throws Exception {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(1L, 2L));
        TableGroup expectedTableGroup = new TableGroup(1L, LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable1 = new OrderTable(1L, expectedTableGroup, 1, false);
        OrderTable orderTable2 = new OrderTable(1L, expectedTableGroup, 1, false);
        expectedTableGroup.getOrderTables().add(orderTable1);
        expectedTableGroup.getOrderTables().add(orderTable2);

        when(tableGroupService.create(List.of(1L, 2L))).thenReturn(expectedTableGroup);

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tableGroupCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 단체_지정_해제에_성공한다() throws Exception {
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
