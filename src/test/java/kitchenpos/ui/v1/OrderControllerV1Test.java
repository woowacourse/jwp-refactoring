package kitchenpos.ui.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineCreateRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
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
@WebMvcTest(OrderControllerV1.class)
class OrderControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("/api/v1/orders로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        var request = new OrderCreateRequest(1L, List.of(
            new OrderLineCreateRequest(2L, 2)
        ));
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        var response = new OrderResponse(4885L, 1L, OrderStatus.COOKING, orderedTime, List.of(
            new OrderLineItemResponse(1L, 2L, 2)
        ));

        given(orderService.create(any(OrderCreateRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/orders/4885"));
    }

    @Test
    @DisplayName("/api/v1/orders로 GET 요청을 보내면 200 응답과 결과가 반환된다.")
    void findAll_with_201() throws Exception {
        // given
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        given(orderService.findAll())
            .willReturn(List.of(
                new OrderResponse(4885L, 1L, OrderStatus.COOKING, orderedTime, List.of(
                    new OrderLineItemResponse(1L, 2L, 2)
                )),
                new OrderResponse(4886L, 2L, OrderStatus.COMPLETION, orderedTime, List.of(
                    new OrderLineItemResponse(2L, 3L, 5)
                )))
            );

        // when & then
        mockMvc.perform(get("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("/api/v1/orders/{id}/order-status로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeOrderStatus_with_200() throws Exception {
        // given
        Long orderId = 1L;
        var request = new OrderUpdateRequest(OrderStatus.COMPLETION);
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        var response = new OrderResponse(4885L, 1L, OrderStatus.COOKING, orderedTime, List.of(
            new OrderLineItemResponse(1L, 2L, 2)
        ));
        given(orderService.changeOrderStatus(anyLong(), any(OrderStatus.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/orders/{id}/order-status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
