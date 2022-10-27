package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.ControllerTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {

    private final String defaultTableGroupUrl = "/api/table-groups";
private final TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.now(), new ArrayList<>());
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void table_group을_생성할_수_있다() throws Exception {
        // given
        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenReturn(tableGroupResponse);

        // when
        ResultActions response = postRequestWithJson(defaultTableGroupUrl, new TableGroupCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", defaultTableGroupUrl + "/" + tableGroupResponse.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(tableGroupResponse)));
    }

    @Test
    void table_group을_취소할_수_있다() throws Exception {
        ResultActions response = deleteRequest(defaultTableGroupUrl + "/1");
        response.andExpect(status().isNoContent());
    }
}
