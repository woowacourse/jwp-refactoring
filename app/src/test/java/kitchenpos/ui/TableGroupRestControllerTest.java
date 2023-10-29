package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.ui.TableGroupRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("새로운 단체를 생성할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(1L, 2L, 3L));

        given(tableGroupService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupCreateRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/table-groups/1"));
    }

    @DisplayName("테이블 Id 목록이 null이면 예외 처리한다.")
    @Test
    void create_FailWhenTableIdsNull() throws Exception {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("테이블 Id 목록의 크키가 0이면 예외 처리한다.")
    @Test
    void create_FailWhenTableIdsSizeIsZero() throws Exception {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of());

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("테이블 Id 목록의 크키가 1이면 예외 처리한다.")
    @Test
    void create_FailWhenTableIdsSizeIsOne() throws Exception {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(1L));

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("테이블 Id 목록에 중복된 Id가 존재하면 예외 처리한다.")
    @Test
    void create_FailWhenTableIdsDuplicate() throws Exception {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(1L, 1L));

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("단체를 주문 테이블에서 할당 해제할 수 있다.")
    @Test
    void unGroup() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1)
                .contentType(APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNoContent());
    }
}
