package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableEmptyRequest;
import kitchenpos.ui.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class TableRestControllerTest extends RestControllerTest {

    @Test
    void 테이블_생성에_성공한다() throws Exception {
        OrderTableRequest request = new OrderTableRequest(5, true);
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 5, true);
        when(tableService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 테이블_목록_조회에_성공한다() throws Exception {
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 5, true);

        when(tableService.list()).thenReturn(Arrays.asList(response));

        MvcResult mvcResult = mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<OrderTable> content = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<OrderTable>>() {
                });

        assertThat(content).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }

    @Test
    void 테이블_비우기_성공() throws Exception {
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 5, true);
        when(tableService.changeEmpty(any())).thenReturn(response);

        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 테이블_게스트_수_변경() throws Exception {
        OrderTableNumberOfGuestsRequest request = new OrderTableNumberOfGuestsRequest(5);
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 5, true);
        when(tableService.changeNumberOfGuests(any(), anyInt())).thenReturn(response);

        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
