package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrderLineItemsRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends KitchenPosControllerTest {

    private static final OrderResponse ORDER;

    static {
        final long orderId = 1L;
        final long tableId = 10L;
        final long orderLineItemSeq = 100L;
        final long menuId = 1_000L;
        final long orderLineItemQuantity = 10_000L;
        final String orderStatus = OrderStatus.COOKING.name();
        final LocalDateTime now = LocalDateTime.now();

        List<OrderLineItemResponse> orderLineItems = Collections.singletonList(
            OrderLineItemResponse.of(orderLineItemSeq, orderId, menuId, orderLineItemQuantity)
        );

        ORDER = OrderResponse.of(orderId, tableId, orderStatus, now, orderLineItems);
    }

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 추가")
    @Test
    void create() throws Exception {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            ORDER.getOrderTableId(),
            Collections.singletonList(new OrderLineItemsRequest(
                ORDER.getOrderLineItems().get(0).getMenuId(),
                ORDER.getOrderLineItems().get(0).getQuantity()
            ))
        );

        given(orderService.create(orderCreateRequest))
            .willReturn(ORDER);

        final ResultActions resultActions = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(orderCreateRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(ORDER.getId().intValue())))
            .andExpect(jsonPath("$.orderTableId", is(ORDER.getOrderTableId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(ORDER.getOrderStatus())))
            .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
            .andExpect(jsonPath("$.orderLineItems[0].seq",
                is(ORDER.getOrderLineItems().get(0).getSeq().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].orderId",
                is(ORDER.getOrderLineItems().get(0).getOrderId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].menuId",
                is(ORDER.getOrderLineItems().get(0).getMenuId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].quantity",
                is((int) ORDER.getOrderLineItems().get(0).getQuantity())))
            .andDo(print());
    }

    @DisplayName("주문 조회")
    @Test
    void list() throws Exception {
        given(orderService.list())
            .willReturn(Collections.singletonList(ORDER));

        final ResultActions resultActions = mockMvc.perform(get("/api/orders")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(ORDER.getId().intValue())))
            .andDo(print());
    }

    @DisplayName("주문 상태 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) throws Exception {
        Long orderId = ORDER.getId();
        OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest(
            orderStatus.name());

        OrderResponse order = OrderResponse
            .of(orderId, ORDER.getOrderTableId(), orderChangeStatusRequest.getOrderStatus(),
                ORDER.getOrderedTime(), ORDER.getOrderLineItems());

        given(orderService.changeOrderStatus(orderId, orderChangeStatusRequest))
            .willReturn(order);

        final ResultActions resultActions = mockMvc
            .perform(put("/api/orders/" + order.getId() + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(orderChangeStatusRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(order.getId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
            .andDo(print());
    }
}
