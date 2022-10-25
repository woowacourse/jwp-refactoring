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

    private OrderTable 주문_테이블;

    private Order 요리중_주문;

    @BeforeEach
    void setUp() {
        주문_테이블 = tableService.create(new OrderTable(null, 3, false));
        요리중_주문 = new Order(주문_테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
    }

    @Test
    void 주문을_생성할_수_있다() {
        주문_항목을_추가한다(요리중_주문);

        Order targetOrder = orderService.create(요리중_주문);

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderStatus()).isEqualTo(targetOrder.getOrderStatus()),
                () -> assertThat(targetOrder.getOrderTableId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> orderService.create(요리중_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_수와_메뉴_수가_같지_않으면_예외가_발생한다() {
        Menu ramen = 메뉴를_생성한다("라면");
        요리중_주문.addOrderLineItem(new OrderLineItem(요리중_주문.getId(), ramen.getId(), 1));
        요리중_주문.addOrderLineItem(new OrderLineItem(요리중_주문.getId(), ramen.getId(), 1));

        assertThatThrownBy(
                () -> orderService.create(요리중_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_적혀있는_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        요리중_주문.setOrderTableId(null);
        주문_항목을_추가한다(요리중_주문);

        assertThatThrownBy(
                () -> orderService.create(요리중_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_테이블이_비어있으면_예외가_발생한다() {
        주문_테이블 = tableService.create(new OrderTable(null, 3, true));
        요리중_주문.setOrderTableId(주문_테이블.getId());
        주문_항목을_추가한다(요리중_주문);

        assertThatThrownBy(
                () -> orderService.create(요리중_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        주문_항목을_추가한다(요리중_주문);
        orderService.create(요리중_주문);

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isOne(),
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(주문_테이블.getId()),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(요리중_주문.getOrderStatus())
        );
    }

    @Test
    void 주문을_변경할_수_있다() {
        주문_항목을_추가한다(요리중_주문);

        요리중_주문 = orderService.create(요리중_주문);
        Order targetOrder = orderService.changeOrderStatus(요리중_주문.getId(), new Order(주문_테이블.getId(),
                OrderStatus.MEAL.name(), LocalDateTime.now()));

        assertThat(targetOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 존재하지_않는_주문을_변경하려_하면_예외가_발생한다() {
        주문_항목을_추가한다(요리중_주문);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(null, new Order(주문_테이블.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now()))
        );
    }

    @Test
    void 배달이_완료된_상태에서_주문을_변경하려_하면_예외가_발생한다() {
        주문_항목을_추가한다(요리중_주문);

        Order targetOrder = orderService.create(요리중_주문);
        targetOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(targetOrder.getId(), targetOrder);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(targetOrder.getId(), new Order(주문_테이블.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now()))
        );
    }
}
