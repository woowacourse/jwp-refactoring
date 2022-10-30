package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;

@DisplayName("Table API 테스트")
class TableRestControllerTest extends RestControllerTest {

    @DisplayName("테이블을 생성한다")
    @Test
    void create() throws Exception {
        final OrderTableRequest request = new OrderTableRequest(1, false);
        final String body = objectMapper.writeValueAsString(request);

        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 1, false);

        BDDMockito.given(tableService.create(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/tables")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/tables/" + response.getId()))
        ;
    }

    @DisplayName("테이블 목록을 조회한다")
    @Test
    void list() throws Exception {
        final OrderTableResponse response = new OrderTableResponse(1L, 1L, 1, false);
        BDDMockito.given(tableService.list())
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/tables"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
        ;
    }

    @DisplayName("테이블이 비어있는지에 대한 여부를 변경한다")
    @Test
    void changeEmpty() throws Exception {
        final long orderTableId = 1L;
        final OrderTableRequest request = new OrderTableRequest(0, true);
        final String body = objectMapper.writeValueAsString(request);

        final OrderTableResponse response = new OrderTableResponse(orderTableId, 1L, 0, true);
        BDDMockito.given(tableService.changeEmpty(eq(orderTableId), any()))
                .willReturn(response);

        mockMvc.perform(put("/api/tables/" + orderTableId + "/empty")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("empty", is(response.isEmpty())))
        ;
    }

    @DisplayName("테이블의 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() throws Exception {
        final long orderTableId = 1L;
        final OrderTableRequest request = new OrderTableRequest(5, false);
        final String body = objectMapper.writeValueAsString(request);

        final OrderTableResponse response = new OrderTableResponse(orderTableId, 1L, 5, false);
        BDDMockito.given(tableService.changeNumberOfGuests(eq(orderTableId), any()))
                .willReturn(response);

        mockMvc.perform(put("/api/tables/" + orderTableId + "/number-of-guests")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("numberOfGuests", is(response.getNumberOfGuests())))
        ;
    }
}
