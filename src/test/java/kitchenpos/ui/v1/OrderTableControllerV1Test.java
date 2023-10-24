package kitchenpos.ui.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.anyBoolean;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.OrderTableUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(OrderTableControllerV1.class)
class OrderTableControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderTableService orderTableService;

    @Test
    @DisplayName("/api/v1/tables로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        var request = new OrderTableCreateRequest(false, 0);
        var response = new OrderTableResponse(1L, false, 0, null);
        given(orderTableService.create(any(OrderTableCreateRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/tables/1"));
    }

    @Test
    @DisplayName("/api/v1/tables로 GET 요청을 보내면 200 응답과 결과가 조회된다.")
    void findAll_with_200() throws Exception {
        // given
        var response = List.of(
            new OrderTableResponse(1L, false, 0, 1L),
            new OrderTableResponse(2L, true, 0, null)
        );
        given(orderTableService.findAll())
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/tables")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("/api/v1/tables/{id}/empty로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeEmpty_with_200() throws Exception {
        // given
        Long tableId = 1L;
        var request = new OrderTableUpdateRequest(true, 1);
        var response = new OrderTableResponse(1L, true, 4885, null);
        given(orderTableService.changeEmpty(anyLong(), anyBoolean()))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/tables/{id}/empty", tableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/v1/tables/{id}/number-of-guests로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeNumberOfGuests_with_200() throws Exception {
        // given
        Long tableId = 1L;
        var request = new OrderTableUpdateRequest(true, 4885);
        var response = new OrderTableResponse(1L, false, 4885, null);
        given(orderTableService.changeNumberOfGuests(anyLong(), anyInt()))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/tables/{id}/number-of-guests", tableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

}
