package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("TableGroup을 생성한다.")
    void create() throws Exception {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable(1L, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, 10, true);
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

        given(tableGroupService.create(any(TableGroup.class))).willReturn(tableGroup);

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableGroup)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/table-groups/1"));
    }

    @Test
    @DisplayName("TableGroup을 해제한다.")
    void ungroup() throws Exception {
        doNothing().when(tableGroupService).ungroup(anyLong());
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}
