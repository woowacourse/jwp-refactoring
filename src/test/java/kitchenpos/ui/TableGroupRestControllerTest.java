package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.ControllerTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    @Test
    void table_group을_생성할_수_있다() throws Exception {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(tableGroup);
        String url = "/api/table-groups";

        // when
        ResultActions response = postRequestWithJson(url, tableGroup);

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", url + "/" + tableGroup.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(tableGroup)));
    }

    @Test
    void table_group을_취소할_수_있다() throws Exception {
        // given
        String url = "/api/table-groups/1";

        // when
        ResultActions response = deleteRequest(url);

        // then
        response.andExpect(status().isNoContent());
    }
}
