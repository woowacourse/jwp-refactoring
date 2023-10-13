package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 테이블_그룹을_저장한다() throws Exception {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(tableGroup);

        // when
        ResultActions result = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup))
        );

        // then
        result.andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/table-groups/" + tableGroup.getId()),
                content().json(objectMapper.writeValueAsString(tableGroup))
        );
    }

    @Test
    void 테이블_그룹을_해제한다() throws Exception {
        // given
        long tableGroupId = 1L;

        // when
        ResultActions result = mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId));

        // then
        result.andExpect(status().isNoContent());
    }
}
