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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 3));

        Order order = new Order(1L, orderLineItems);
        Order ordered = orderService.create(order);

        assertThat(ordered.getId()).isEqualTo(1L);
        assertThat(ordered.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_정보에_테이블_ID가_없는_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 3));

        Order order = new Order(null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_주문_메뉴_리스트가_없을_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        Order order = new Order(1L, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_테이블이_존재하지_않는_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 3));

        Order order = new Order(100L, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_테이블이_EMPTY_상태인_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 3));

        Order order = new Order(2L, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_정보에_메뉴가_존재하지_않는_경우_예외_발생() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(100L, 3));

        Order order = new Order(1L, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }
}
