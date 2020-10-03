package kitchenpos.ui;

import kitchenpos.TestObjectFactory;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 생성 요청 테스트")
    @Test
    void create() throws Exception {
        OrderTable orderTable1 = TestObjectFactory.creatOrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = TestObjectFactory.creatOrderTable();
        orderTable2.setId(2L);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroup(orderTables);
        tableGroup.setId(1L);

        given(tableGroupService.create(any())).willReturn(tableGroup);

        mockMvc.perform(post("/api/table-groups/")
                .content("{\n"
                        + "  \"orderTables\": [\n"
                        + "    {\n"
                        + "      \"id\": 1\n"
                        + "    },\n"
                        + "    {\n"
                        + "      \"id\": 2\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderTables", Matchers.hasSize(2)));
    }

    @DisplayName("테이블 그룹을 해지하는 요청 테스트")
    @Test
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
