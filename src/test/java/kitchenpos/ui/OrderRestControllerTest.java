package kitchenpos.ui;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 문서화 테스트")
class OrderRestControllerTest extends ApiDocument {
    @DisplayName("주문 저장 - 성공")
    @Test
    void order_create() throws Exception {
        //given
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Collections.singletonList(OrderLineItemFixture.FIRST_FIRST_ORDERLINE_NO_SEQ_NO_ORDER));
        //when
        willReturn(OrderFixture.FIRST_TABLE_후라이드치킨_하나).given(orderService).create(any(Order.class));
        final ResultActions result = 주문_저장_요청(order);
        //then
        주문_저장_성공함(result, OrderFixture.FIRST_TABLE_후라이드치킨_하나);
    }

    @DisplayName("주문 조회 - 성공")
    @Test
    void order_findAll() throws Exception {
        //given
        //when
        willReturn(OrderFixture.orders()).given(orderService).list();
        final ResultActions result = 주문_조회_요청();
        //then
        주문_조회_성공함(result, OrderFixture.orders());
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void order_change_order_status() throws Exception {
        //given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        //when
        willReturn(OrderFixture.FIRST_TABLE_후라이드치킨_하나_COMPLETION).given(orderService)
                .changeOrderStatus(anyLong(), any(Order.class));
        final ResultActions result = 주문_상태_변경_요청(OrderFixture.FIRST_TABLE_후라이드치킨_하나.getId(), order);
        //then
        주문_상태_변경_성공함(result, OrderFixture.FIRST_TABLE_후라이드치킨_하나_COMPLETION);
    }

    private ResultActions 주문_저장_요청(Order order) throws Exception {
        return mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(order))
        );
    }

    private ResultActions 주문_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/orders"));
    }

    private ResultActions 주문_상태_변경_요청(Long id, Order order) throws Exception {
        return mockMvc.perform(put("/api/orders/{orderId}/order-status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(order))
        );
    }

    private void 주문_저장_성공함(ResultActions result, Order order) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(order)))
                .andExpect(header().string("Location", "/api/orders/" + order.getId()))
                .andDo(toDocument("order-create"));
    }

    private void 주문_조회_성공함(ResultActions result, List<Order> orders) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orders)))
                .andDo(toDocument("order-findAll"));
    }

    private void 주문_상태_변경_성공함(ResultActions result, Order order) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(order)))
                .andDo(toDocument("order-change-status"));
    }
}
