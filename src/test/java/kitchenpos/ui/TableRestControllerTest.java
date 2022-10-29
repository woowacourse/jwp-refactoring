package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import kitchenpos.dto.request.NumberOfGuestsChangeRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.TableEmptyChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class TableRestControllerTest extends RestControllerTest {

    @Test
    void 주문_테이블_생성에_성공한다() throws Exception {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);
        OrderTableResponse expected = new OrderTableResponse(1L, null, 0, true);
        when(tableService.create(refEq(request)))
                .thenReturn(expected);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 주문_테이블_목록_조회에_성공한다() throws Exception {
        OrderTableResponse expected = new OrderTableResponse(1L, null, 0, true);
        when(tableService.list()).thenReturn(List.of(expected));

        MvcResult mvcResult = mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<OrderTableResponse> content = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<OrderTableResponse>>() {
                });
        assertThat(content).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경에_성공한다() throws Exception {
        TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);
        OrderTableResponse expected = new OrderTableResponse(1L, null, 0, true);
        when(tableService.changeEmpty(1L, true)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        OrderTableResponse content = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), OrderTableResponse.class
        );
        assertThat(content.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_인원_수_변경에_성공한다() throws Exception {
        NumberOfGuestsChangeRequest request = new NumberOfGuestsChangeRequest(1);
        OrderTableResponse expected = new OrderTableResponse(1L, null, 1, true);
        when(tableService.changeNumberOfGuests(1L, 1)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        OrderTableResponse content = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), OrderTableResponse.class
        );
        assertThat(content.getNumberOfGuests()).isOne();
    }
}
