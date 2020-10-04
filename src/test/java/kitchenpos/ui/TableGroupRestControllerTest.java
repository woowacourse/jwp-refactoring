package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    private static final long TABLE_GROUP_ID = 1L;
    private static final long ORDER_TABLES_FIRST_ID = 2L;
    private static final long ORDER_TABLES_SECOND_ID = 3L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() throws Exception {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(createdOrderTable(ORDER_TABLES_FIRST_ID));
        orderTables.add(createdOrderTable(ORDER_TABLES_SECOND_ID));

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(TABLE_GROUP_ID);
        tableGroup.setOrderTables(orderTables);

        String body = "{\n"
            + "  \"orderTables\": [\n"
            + "    {\n"
            + "      \"id\": " + orderTables.get(0).getId() + "\n"
            + "    },\n"
            + "    {\n"
            + "      \"id\": " + orderTables.get(1).getId() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        given(tableGroupService.create(any()))
            .willReturn(tableGroup);

        ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(tableGroup.getId().intValue())))
            .andExpect(jsonPath("$.orderTables", hasSize(2)))
            .andExpect(jsonPath("$.orderTables[0].id", is(orderTables.get(0).getId().intValue())))
            .andExpect(jsonPath("$.orderTables[1].id", is(orderTables.get(1).getId().intValue())))
            .andDo(print());
    }

    @DisplayName("테이블 그룹 제거")
    @Test
    void ungroup() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/table-groups/" + TABLE_GROUP_ID)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    private OrderTable createdOrderTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }
}
