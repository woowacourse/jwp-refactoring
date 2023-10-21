package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.fakedao.InMemoryMenuDao;
import kitchenpos.dao.fakedao.InMemoryOrderDao;
import kitchenpos.dao.fakedao.InMemoryOrderLineItemDao;
import kitchenpos.dao.fakedao.InMemoryOrderTableDao;
import kitchenpos.domain.MenuFactory;
import kitchenpos.domain.OrderFactory;
import kitchenpos.domain.OrderLineItemFactory;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableFactory;
import kitchenpos.domain.menugroup.MenuGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private OrderLineItemDao orderLineItemDao;

    @BeforeEach
    void setUp() {
        menuDao = new InMemoryMenuDao();
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
        orderLineItemDao = new InMemoryOrderLineItemDao();
    }

    @Nested
    class 주문_등록시 {

        @Test
        void 주문한_메뉴가_하나도_없다면_에외가_발생한다() {
            // given
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));
            final var order = OrderFactory.createOrderOf(table.getId());
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문한_메뉴가_등록되지_않은_메뉴라면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup);
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = OrderFactory.createOrderOf(table.getId(), orderLineItem);
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_사용된_테이블이_등록되있지_않으면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);

            final var order = OrderFactory.createOrderOf(Long.MAX_VALUE, orderLineItem);
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_사용된_테이블이_비어있다면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var order = OrderFactory.createOrderOf(table.getId(), orderLineItem);
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있지_않고_메뉴가_존재하면_정상적으로_등록된다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = OrderFactory.createOrderOf(table.getId(), orderLineItem);
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final var savedOrder = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING"),
                    () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                    () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
            );
        }
    }

    @Nested
    class 주문_상태_변경시 {

        @Test
        void 계산완료_상태일_경우_더_이상_상태를_변경할_수_없다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var order = orderDao.save(OrderFactory.createOrderOf(table.getId(), orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            order.setOrderStatus(OrderStatus.COMPLETION.name());

            final var nextOrder = OrderFactory.createOrderOf(3L, orderLineItem);
            nextOrder.setOrderStatus(OrderStatus.MEAL.name());

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 식사중일_경우_조리중으로_변경할_수_없다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var order = orderDao.save(OrderFactory.createOrderOf(table.getId(), orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            order.setOrderStatus(OrderStatus.MEAL.name());

            final var nextOrder = OrderFactory.createOrderOf(3L, orderLineItem);
            nextOrder.setOrderStatus(OrderStatus.COOKING.name());

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {"COOKING: MEAL", "MEAL:COMPLETION"}, delimiter = ':')
        void 변경_가능한_상태일경우_정상적으로_변경한다(OrderStatus previous, OrderStatus next) {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var order = orderDao.save(OrderFactory.createOrderOf(table.getId(), orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            order.setOrderStatus(previous.name());

            final var nextOrder = OrderFactory.createOrderOf(1L, orderLineItem);
            nextOrder.setOrderStatus(next.name());

            // when
            final var savedOrder = orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThat(savedOrder.getOrderStatus()).isEqualTo(next.name());
        }
    }
}
