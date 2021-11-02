package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.response.OrderLineItemResponse;
import kitchenpos.ui.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItemRequest> 주문_목록_생성 = 주문_목록_생성();
        OrderRequest order = new OrderRequest(1L, 주문_목록_생성);
        List<OrderLineItemResponse> collect = 주문_목록_생성.stream()
            .map(value -> new OrderLineItemResponse(1L, 1L, value.getMenuId(), value.getQuantity()))
            .collect(Collectors.toList());
        OrderResponse expected = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now().toString(), collect);
        given(orderService.create(any(OrderRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(order)));

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/orders/%s", expected.getId())))
            .andExpect(content().string(objectToJson(expected)));
    }

    private List<OrderLineItemRequest> 주문_목록_생성() {
        OrderLineItemRequest 후라이드_치킨 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest 모짜_치즈볼_5pc = new OrderLineItemRequest(2L, 5L);
        OrderLineItemRequest 치킨윙_4pc = new OrderLineItemRequest(3L, 4L);
        OrderLineItemRequest 맥주_500_cc = new OrderLineItemRequest(4L, 1L);

        return Arrays.asList(후라이드_치킨, 모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        OrderLineItemResponse 후라이드_치킨 = new OrderLineItemResponse(1L, 1L, 1L, 1L);
        OrderLineItemResponse 모짜_치즈볼_5pc = new OrderLineItemResponse(2L, 2L, 2L, 5L);
        OrderLineItemResponse 치킨윙_4pc = new OrderLineItemResponse(3L, 3L, 3L, 4L);
        OrderLineItemResponse 맥주_500_cc = new OrderLineItemResponse(4L, 4L, 4L, 1L);
        List<OrderLineItemResponse> orderLineItemsA = Arrays.asList(후라이드_치킨, 맥주_500_cc);
        List<OrderLineItemResponse> orderLineItemsB = Arrays.asList(모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc);
        OrderResponse orderA = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now().toString(), orderLineItemsA);
        OrderResponse orderB = new OrderResponse(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now().toString(), orderLineItemsB);
        List<OrderResponse> expected = Arrays.asList(orderA, orderB);

        given(orderService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/orders"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("주문을 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        Long orderId = 1L;
        List<OrderLineItemRequest> 주문_목록_생성 = 주문_목록_생성();
        List<OrderLineItemResponse> collect = 주문_목록_생성.stream()
            .map(value -> new OrderLineItemResponse(1L, 1L, value.getMenuId(), value.getQuantity()))
            .collect(Collectors.toList());
        OrderRequest order = new OrderRequest(1L, 주문_목록_생성);
        OrderResponse expected = new OrderResponse(orderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.now().toString(), collect);
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/orders/%s/order-status", orderId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(order))
        );

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
