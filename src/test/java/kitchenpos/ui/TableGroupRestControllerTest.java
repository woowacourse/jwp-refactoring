package kitchenpos.ui;

import kitchenpos.RestControllerTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends RestControllerTest {

    @MockBean
    private TableGroupService mockTableGroupService;

    @DisplayName("테이블 그룹 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        TableGroup requestTableGroup = createTableGroup();
        when(mockTableGroupService.create(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(post("/api/table-groups")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTableGroup))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + requestTableGroup.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(requestTableGroup)));
    }

    @DisplayName("테이블 그룹 해제 요청을 처리한다.")
    @Test
    void ungroup() throws Exception {
        Long tableGroupId = 1L;
        doNothing().when(mockTableGroupService).ungroup(tableGroupId);
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId))
                .andExpect(status().isNoContent());
    }
}
