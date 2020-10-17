package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.dto.tablegroup.OrderTableDto;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
        OrderTableDto orderTable1 = new OrderTableDto(1L, 1L, 0, false);
        OrderTableDto orderTable2 = new OrderTableDto(2L, 1L, 0, false);
        List<OrderTableDto> orderTableDtos = Arrays.asList(orderTable1, orderTable2);

        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, orderTableDtos, LocalDateTime.now());

        given(tableGroupService.create(any())).willReturn(tableGroupResponse);

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
                .andExpect(header().string("Location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.createdDate", Matchers.instanceOf(String.class)))
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
