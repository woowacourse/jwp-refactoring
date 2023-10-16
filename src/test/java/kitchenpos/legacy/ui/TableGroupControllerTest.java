package kitchenpos.legacy.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.legacy.application.LegacyTableGroupService;
import kitchenpos.legacy.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(TableGroupController.class)
class TableGroupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LegacyTableGroupService tableGroupService;

    @Test
    @DisplayName("/api/table-groups로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        TableGroup request = new TableGroup();
        TableGroup response = new TableGroup();
        response.setId(1L);
        given(tableGroupService.create(any(TableGroup.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/table-groups/1"));
    }

    @Test
    @DisplayName("/api/table-groups/{id}로 DELETE 요청을 보내면 204 응답이 반환된다.")
    void ungroup_with_204() throws Exception {
        // given
        Long tableGroupId = 1L;

        // when & then
        mockMvc.perform(delete("/api/table-groups/{id}", tableGroupId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
