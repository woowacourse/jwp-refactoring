package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = tableService.create(new OrderTable(null, 3, false));
    }

    @Test
    void 주문을_생성할_수_있다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        Order targetOrder = orderService.create(order);

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(targetOrder.getOrderTableId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_수와_메뉴_수가_같지_않으면_예외가_발생한다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        Menu ramen = 메뉴를_생성한다("라면");
        order.addOrderLineItem(new OrderLineItem(order.getId(), ramen.getId(), 1));
        order.addOrderLineItem(new OrderLineItem(order.getId(), ramen.getId(), 1));

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_적혀있는_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        Order order = new Order(null, OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_테이블이_비어있지_않으면_예외가_발생한다() {
        orderTable = tableService.create(new OrderTable(null, 3, true));
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);
        orderService.create(order);

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isOne(),
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus())
        );
    }

    @Test
    void 주문을_변경할_수_있다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        Order savedOrder = orderService.create(order);
        Order targetOrder = orderService.changeOrderStatus(savedOrder.getId(), new Order(orderTable.getId(),
                OrderStatus.MEAL.name(), LocalDateTime.now()));

        assertThat(targetOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 존재하지_않는_주문을_변경하려_하면_예외가_발생한다() {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(null, new Order(orderTable.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now()))
        );
    }

    @Test
    void 배달이_완료된_상태에서_주문을_변경하려_하면_예외가_발생한다() {
        Order order = new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());
        주문_항목을_추가한다(order);

        Order targetOrder = orderService.create(order);
        targetOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(targetOrder.getId(), targetOrder);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(targetOrder.getId(), new Order(orderTable.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now()))
        );
    }
}
