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
import kitchenpos.Constructor;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends Constructor {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItem> 주문_목록_생성 = 주문_목록_생성();
        Order order = orderConstructor(주문_목록_생성);
        Order expected = orderConstructor(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_목록_생성);
        given(orderService.create(any(Order.class))).willReturn(expected);

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

    private List<OrderLineItem> 주문_목록_생성() {
        OrderLineItem 후라이드_치킨 = orderLineItemConstructor(1L, 1L, 1L, 1);
        OrderLineItem 모짜_치즈볼_5pc = orderLineItemConstructor(2L, 2L, 2L, 5);
        OrderLineItem 치킨윙_4pc = orderLineItemConstructor(3L, 3L, 3L, 4);
        OrderLineItem 맥주_500_cc = orderLineItemConstructor(4L, 4L, 4L, 1);

        return Arrays.asList(후라이드_치킨, 모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        OrderLineItem 후라이드_치킨 = orderLineItemConstructor(1L, 1L, 1L, 1);
        OrderLineItem 모짜_치즈볼_5pc = orderLineItemConstructor(2L, 2L, 2L, 5);
        OrderLineItem 치킨윙_4pc = orderLineItemConstructor(3L, 3L, 3L, 4);
        OrderLineItem 맥주_500_cc = orderLineItemConstructor(4L, 4L, 4L, 1);
        List<OrderLineItem> orderLineItemsA = Arrays.asList(후라이드_치킨, 맥주_500_cc);
        List<OrderLineItem> orderLineItemsB = Arrays.asList(모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc);
        Order orderA = orderConstructor(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItemsA);
        Order orderB = orderConstructor(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItemsB);
        List<Order> expected = Arrays.asList(orderA, orderB);

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
        List<OrderLineItem> 주문_목록_생성 = 주문_목록_생성();
        Order order = orderConstructor(주문_목록_생성);
        Order expected = orderConstructor(orderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문_목록_생성);
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
