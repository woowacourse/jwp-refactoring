package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.fake.FakeMenuDao;
import kitchenpos.fake.FakeOrderDao;
import kitchenpos.fake.FakeOrderLineItemDao;
import kitchenpos.fake.FakeOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {

    private OrderDao orderDao = new FakeOrderDao();
    private OrderLineItemDao orderLineItemDao = new FakeOrderLineItemDao();
    private OrderTableDao orderTableDao = new FakeOrderTableDao();
    private MenuDao menuDao = new FakeMenuDao();

    private OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    private OrderLineItem orderLineItem;
    private OrderTable tableFull;
    private OrderTable tableEmpty;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct = new MenuProduct(null, null, new Product(null, "후라이드", BigDecimal.valueOf(2000)), 1L);
        menuDao.save(new Menu(null, "후라이드치킨", BigDecimal.valueOf(2000), 1L, List.of(menuProduct)));
        orderLineItem = orderLineItemDao.save(new OrderLineItem(null, 1L, 1L, 1L));
        tableFull = orderTableDao.save(new OrderTable(null, 3, false));
        tableEmpty = orderTableDao.save(new OrderTable(null, 4, true));
    }

    @Test
    void 주문을_생성_한다() {
        Order order = new Order(null, tableFull, List.of(orderLineItem));
        Order saved = orderService.create(order);

        assertThat(order).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void 주문_상태를_변경한다() {
        Order order = new Order(null, tableFull, List.of(orderLineItem));
        Order saved = orderService.create(order);

        Order changeOrderStatus = orderService.changeOrderStatus(saved.getId(), OrderStatus.MEAL);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo("MEAL");
    }

    @Test
    void 완료_상태에서_주문_상태를_변경할_수_없다() {
        Order order = new Order(null, tableFull, List.of(orderLineItem));
        Order saved = orderService.create(order);
        orderService.changeOrderStatus(saved.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(saved.getId(), OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_전체_조회를_한다() {
        orderService.create(new Order(null, tableFull, List.of(orderLineItem)));
        orderService.create(new Order(null, tableFull, List.of(orderLineItem)));

        assertThat(orderService.list()).hasSize(2);
    }

    @Nested
    class 주문을_생성_할_때 {

        @Test
        void 빈_테이블에서_주문할_수_없다() {
            assertThatThrownBy(() -> orderService.create(new Order(null, tableEmpty, List.of(orderLineItem))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_메뉴를_주문할_수_없다() {
            long notExistMenuId = 2L;
            assertThatThrownBy(() -> orderService.create(new Order(null, tableFull, List.of(new OrderLineItem(null, 1L, notExistMenuId, 1L)))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_시간을_기록한다() {
            Order order = new Order(null, tableFull, List.of(orderLineItem));
            Order saved = orderService.create(order);

            assertThat(saved.getOrderedTime()).isNotNull();
        }

        @Test
        void 주문_상태를_COOKING으로_변경한다() {
            Order order = new Order(null, tableFull, List.of(orderLineItem));
            Order saved = orderService.create(order);

            assertThat(saved.getOrderStatus()).isEqualTo("COOKING");
        }

        @Test
        void 존재하지_않는_테이블에서_주문할_수_없다() {
            OrderTable table = new OrderTable(null, 3, false);
            assertThatThrownBy(() -> orderService.create(new Order(null, table, List.of(orderLineItem))))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
