package ordertable.test.java.kitchenpos.tablegroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableRequest;
import kitchenpos.tablegroup.ui.TableGroupRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @Test
    void create() throws Exception {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 10, true);
        final TableGroupRequest request = new TableGroupRequest(List.of(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));
        final TableGroup responseTableGroup = new TableGroup(1L, null, List.of(orderTable1, orderTable2));
        given(tableGroupService.create(any())).willReturn(responseTableGroup);

        // when & then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/table-groups/1"));
    }

    @Test
    void ungroup() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(any());

        // when & then
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}
