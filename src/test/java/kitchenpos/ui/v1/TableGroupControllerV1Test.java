package kitchenpos.ui.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
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
@WebMvcTest(TableGroupControllerV1.class)
class TableGroupControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TableGroupService tableGroupService;

    @Test
    @DisplayName("/api/v1/table-groups로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        var request = new TableGroupCreateRequest(List.of(4885L, 4886L));
        LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
        var response = new TableGroupResponse(1L, createdDate, List.of(4885L, 4886L));
        given(tableGroupService.create(any(TableGroupCreateRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/table-groups/1"));
    }

    @Test
    @DisplayName("/api/v1/table-groups/{id}로 DELETE 요청을 보내면 204 응답이 반환된다.")
    void ungroup_with_204() throws Exception {
        // given
        Long tableGroupId = 1L;

        // when & then
        mockMvc.perform(delete("/api/v1/table-groups/{id}", tableGroupId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
