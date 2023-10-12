package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    private Menu menu;
    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
        menu = menuDao.save(new Menu("치킨 세트 메뉴", new BigDecimal(20000), menuGroup.getId(), null));
        tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 6, false));
    }

    @Nested
    class create_메서드는 {
        @Test
        void 주문을_생성한다() {
            // given
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
            final Order order = new Order(
                    orderTable.getId(),
                    null,
                    List.of(orderLineItem)
            );

            // when
            final Order createdOrder = orderService.create(order);

            // then
            final Order expected = new Order(
                    1L,
                    orderTable.getId(),
                    OrderStatus.COOKING.name(),
                    List.of(orderLineItem)
            );

            assertSoftly(
                    softly -> {
                        softly.assertThat(createdOrder)
                                .usingRecursiveComparison()
                                .ignoringFields("orderedTime", "orderLineItems")
                                .isEqualTo(expected);
                        softly.assertThat(createdOrder.getOrderLineItems()).hasSize(1);
                    }
            );
        }

        @Test
        void 주문_항목이_비어있으면_예외가_발생한다() {
            // given
            final Order order = new Order(
                    orderTable.getId(),
                    null,
                    Collections.emptyList()
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목에_존재하지_않는_메뉴가_있으면_예외가_발생한다() {
            // given
            final OrderLineItem orderLineItem = new OrderLineItem(null, 99L, 1);
            final Order order = new Order(
                    orderTable.getId(),
                    null,
                    List.of(orderLineItem)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_메뉴가_중복되면_예외가_발생한다() {
            // given
            final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu.getId(), 1);
            final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu.getId(), 2);
            final Order order = new Order(
                    orderTable.getId(),
                    null,
                    List.of(orderLineItem1, orderLineItem2)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu.getId(), 1);
            final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu.getId(), 2);
            final Order order = new Order(
                    99L,
                    null,
                    List.of(orderLineItem1, orderLineItem2)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list_메서드는_모든_주문을_조회한다() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        final Order order1 = new Order(
                orderTable.getId(),
                null,
                List.of(orderLineItem)
        );
        final Order order2 = new Order(
                orderTable.getId(),
                null,
                List.of(orderLineItem)
        );
        final Order createdOrder1 = orderService.create(order1);
        final Order createdOrder2 = orderService.create(order2);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .isEqualTo(List.of(createdOrder1, createdOrder2));
    }

    @Nested
    class changeOrderStatus_메서드는 {
        @Test
        void 주문_상태를_변경한다() {
            // given
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
            final Order order = new Order(
                    orderTable.getId(),
                    null,
                    List.of(orderLineItem)
            );
            final Order createdOrder = orderService.create(order);

            // when
            order.setOrderStatus(OrderStatus.MEAL.name());
            final Order actual = orderService.changeOrderStatus(createdOrder.getId(), order);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 존재하지_않는_주문의_상태를_변경하면_예외가_발생한다() {
            // given
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
            final Order uncreatedOrder = new Order(
                    orderTable.getId(),
                    OrderStatus.MEAL.name(),
                    List.of(orderLineItem)
            );

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(uncreatedOrder.getId(), uncreatedOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 계산이_완료된_주문의_상태를_변경하면_예외가_발생한다() {
            // given
            final Order order = new Order(
                    orderTable.getId(),
                    OrderStatus.COMPLETION.name(),
                    LocalDateTime.now(),
                    Collections.emptyList()
            );
            final Order completedOrder = orderDao.save(order);

            // when & then
            completedOrder.setOrderStatus(OrderStatus.MEAL.name());
            assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), completedOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
