package kitchenpos.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    TableService tableService;

    @BeforeEach
    void setUp() {
        tableService.changeEmpty(1L, new OrderTable(0, false));
    }

    @Test
    void 주문_등록_성공() {
        Order ordered = 주문_등록(1L);

        assertThat(ordered.getId()).isEqualTo(1L);
        assertThat(ordered.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_정보에_테이블_ID가_없는_경우_예외_발생() {
        assertThatThrownBy(() -> 주문_등록(null, new OrderLineItem(1L, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_주문_메뉴_리스트가_없을_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        Order order = new Order(1L, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_테이블이_존재하지_않는_경우_예외_발생() {
        assertThatThrownBy(() -> 주문_등록(100L, new OrderLineItem(1L, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_테이블이_EMPTY_상태인_경우_예외_발생() {
        assertThatThrownBy(() -> 주문_등록(2L, new OrderLineItem(1L, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_메뉴가_존재하지_않는_경우_예외_발생() {
        assertThatThrownBy(() -> 주문_등록(null, new OrderLineItem(100L, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문_조회_성공() {
        int FIND_ORDER_SIZE = 3;

        for(int i=0; i<FIND_ORDER_SIZE; i++) {
            Order ordered = 주문_등록(1L);
        }

        assertThat(orderService.list().size()).isEqualTo(FIND_ORDER_SIZE);
    }

    @Test
    void 주문_상태_변경_성공() {
        Order ordered = 주문_등록(1L);

        Order mealed = orderService.changeOrderStatus(ordered.getId(), new Order(OrderStatus.MEAL.name()));

        assertThat(mealed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 변경하려는_주문_상태가_존재하지_않는_상태인_경우_예외발생() {
        Order ordered = 주문_등록(1L);

        assertThatThrownBy(() -> orderService.changeOrderStatus(ordered.getId(), new Order("FINISHED")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_완료된_주문_상태를_변경하려는_경우_예외발생() {
        Order ordered = 주문_등록(1L);
        Order mealed = orderService.changeOrderStatus(ordered.getId(), new Order(OrderStatus.COMPLETION.name()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(mealed.getId(), new Order(OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order 주문_등록(Long tableId, OrderLineItem ... inputOrderLineInItems) {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 3));

        if(inputOrderLineInItems.length > 0) {
            orderLineItems = new ArrayList(Arrays.asList(inputOrderLineInItems));
        }

        Order order = new Order(tableId, orderLineItems);
        return orderService.create(order);
    }
}
