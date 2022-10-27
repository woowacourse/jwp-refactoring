package kitchenpos.presentation;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class TableControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문테이블을 등록한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(new OrderTableRequest(0, true));

        given(tableService.create(any()))
                .willReturn(new OrderTableResponse(1L, null, 0, true));

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/tables")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }
}
