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

    private final String defaultTableGroupUrl = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @Test
    void table_group을_생성할_수_있다() throws Exception {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(tableGroup);

        // when
        ResultActions response = postRequestWithJson(defaultTableGroupUrl, tableGroup);

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", defaultTableGroupUrl + "/" + tableGroup.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(tableGroup)));
    }

    @Test
    void table_group을_취소할_수_있다() throws Exception {
        ResultActions response = deleteRequest(defaultTableGroupUrl + "/1");
        response.andExpect(status().isNoContent());
    }
}
