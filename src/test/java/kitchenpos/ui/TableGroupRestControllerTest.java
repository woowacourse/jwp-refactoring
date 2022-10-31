package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.request.OrderTableIdRequest;
import kitchenpos.ui.dto.request.TableGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends RestControllerTest {

    @Test
    void 테이블_생성에_성공한다() throws Exception {
        final TableGroupRequest request = new TableGroupRequest(
                Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 5, true);
        when(tableService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

}
