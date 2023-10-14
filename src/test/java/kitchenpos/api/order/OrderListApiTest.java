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
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderListApiTest extends ApiTestConfig {

    @DisplayName("주문 전체 조회 API 테스트")
    @Test
    void listOrder() throws Exception {
        // given
        // FIXME: domain -> dto 로 변경
        final Order expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setOrderStatus(OrderStatus.COOKING.name());
        expectedOrder.setOrderedTime(LocalDateTime.now().minusMinutes(20));
        expectedOrder.setOrderTableId(1L);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(2L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(expectedOrder.getId());

        expectedOrder.setOrderLineItems(List.of(orderLineItem));

        // when
        when(orderService.list()).thenReturn(List.of(expectedOrder));

        // then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(expectedOrder.getId().intValue())))
                .andExpect(jsonPath("$[0].orderStatus", is(expectedOrder.getOrderStatus())))
                .andExpect(jsonPath("$[0].orderedTime", is(expectedOrder.getOrderedTime().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].orderTableId", is(expectedOrder.getOrderTableId().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems.size()", is(1)))
                .andExpect(jsonPath("$[0].orderLineItems[0].seq", is(expectedOrder.getOrderLineItems().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems[0].orderId", is(expectedOrder.getOrderLineItems().get(0).getOrderId().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems[0].menuId", is(expectedOrder.getOrderLineItems().get(0).getMenuId().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems[0].quantity", is(Long.valueOf(expectedOrder.getOrderLineItems().get(0).getQuantity()).intValue())));
    }
}
