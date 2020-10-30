package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends MvcTest {

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("/api/table-groups로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(tableGroupService.create(any())).willReturn(TABLE_GROUP);

        TableGroupCreateRequest tableGroupCreateRequest =
            new TableGroupCreateRequest(Arrays.asList(TABLE_ID_1));
        String inputJson = objectMapper.writeValueAsString(tableGroupCreateRequest);
        MvcResult mvcResult = postAction("/api/table-groups", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/table-groups/%d", TABLE_GROUP_ID)))
            .andReturn();

        TableGroup tableGroupResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TableGroup.class);
        assertThat(tableGroupResponse).usingRecursiveComparison().isEqualTo(TABLE_GROUP);
    }

    @DisplayName("/api/table-groups/{tableGroupId}로 DELETE요청 성공 테스트")
    @Test
    void ungroupTest() throws Exception {
        willDoNothing().given(tableGroupService).ungroup(anyLong());

        deleteAction(String.format("/api/table-groups/%d", TABLE_GROUP_ID))
            .andExpect(status().isNoContent());
    }
}