package kitchenpos.ui.table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.dto.table.response.TableGroupResponse;
import kitchenpos.ui.product.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends RestControllerTest {

    @Test
    void 단체_지정에_성공한다() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);
        OrderTableResponse orderTable1 = new OrderTableResponse(1L, null, 1, false);
        OrderTableResponse orderTable2 = new OrderTableResponse(1L, null, 1, false);
        TableGroupResponse expected =
                new TableGroupResponse(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 단체_지정_해제에_성공한다() throws Exception {
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
