package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends ControllerTest {

    @Test
    void 테이블_그룹_생성() throws Exception {
        // given
        TableGroup tableGroup = 테이블_그룹();
        String request = objectMapper.writeValueAsString(tableGroup);
        TableGroup savedTableGroup = 테이블_그룹(1L);
        given(tableGroupService.create(any())).willReturn(savedTableGroup);
        String response = objectMapper.writeValueAsString(savedTableGroup);

        // when & then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 테이블_그룹_제거() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(anyLong());

        // when & then
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}
