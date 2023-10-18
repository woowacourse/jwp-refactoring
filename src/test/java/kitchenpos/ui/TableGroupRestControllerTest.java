package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("POST /api/table-groups - TableGroup 생성")
    public void create() throws Exception {
        //given
        final TableGroup tableGroup = new TableGroup(1L, null, List.of());
        given(tableGroupService.create(any(TableGroup.class))).willReturn(tableGroup);

        //when & then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTables").isArray());
    }

    @Test
    @DisplayName("DELETE /api/table-groups/{tableGroupId} - TableGroup 삭제")
    public void ungroup() throws Exception {
        //given
        final Long tableGroupId = 1L;

        //when & then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
