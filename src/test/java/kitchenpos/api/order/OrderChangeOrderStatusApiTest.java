package kitchenpos.api.order;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderChangeOrderStatusApiTest extends ApiTestConfig {

    @DisplayName("주문 상태 변경 API 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        final String request = "{\n" +
                "  \"orderStatus\": \"MEAL\"\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long orderId = 1L;
        final Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        expectedOrder.setOrderStatus(OrderStatus.MEAL.name());
        expectedOrder.setOrderedTime(LocalDateTime.now().minusHours(1));
        expectedOrder.setOrderTableId(1L);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(2L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(expectedOrder.getId());

        expectedOrder.setOrderLineItems(List.of(orderLineItem));
        when(orderService.changeOrderStatus(eq(orderId), any(Order.class))).thenReturn(expectedOrder);

        // then
        mockMvc.perform(put("/api/orders/{id}/order-status", orderId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(expectedOrder.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(expectedOrder.getOrderStatus())))
                .andExpect(jsonPath("$.orderedTime", is(expectedOrder.getOrderedTime().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.orderTableId", is(expectedOrder.getOrderTableId().intValue())))
                .andExpect(jsonPath("$.orderLineItems.size()", is(1)))
                .andExpect(jsonPath("$.orderLineItems[0].seq", is(expectedOrder.getOrderLineItems().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$.orderLineItems[0].orderId", is(expectedOrder.getOrderLineItems().get(0).getOrderId().intValue())))
                .andExpect(jsonPath("$.orderLineItems[0].menuId", is(expectedOrder.getOrderLineItems().get(0).getMenuId().intValue())))
                .andExpect(jsonPath("$.orderLineItems[0].quantity", is(Long.valueOf(expectedOrder.getOrderLineItems().get(0).getQuantity()).intValue())));
    }
}
