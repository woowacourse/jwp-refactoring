package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.dto.TableGroupCreateRequest;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {

    private static final String TABLE_GROUP_URL = "/api/table-groups";

    private final TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.now(),
            new ArrayList<>());
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void table_group을_생성할_수_있다() throws Exception {
        // given
        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenReturn(tableGroupResponse);

        // when
        ResultActions response = postRequestWithJson(TABLE_GROUP_URL, new TableGroupCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", TABLE_GROUP_URL + "/" + tableGroupResponse.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(tableGroupResponse)));
    }

    @Test
    void table_group을_취소할_수_있다() throws Exception {
        ResultActions response = deleteRequest(TABLE_GROUP_URL + "/1");
        response.andExpect(status().isNoContent());
    }
}
