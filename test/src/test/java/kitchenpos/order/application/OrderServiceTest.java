package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import application.OrderService;
import domain.MenuGroup;
import domain.Order;
import domain.OrderLineItem;
import domain.OrderStatus;
import exception.EntityNotFoundException;
import exception.InvalidOrderException;
import exception.InvalidOrderStatusException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.MenuFactory;
import kitchenpos.menu.dao.InMemoryMenuDao;
import kitchenpos.order.dao.InMemoryOrderDao;
import kitchenpos.table.OrderTableFactory;
import kitchenpos.table.dao.InMemoryOrderTableDao;
import ordertable.repository.OrderTableDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import repository.MenuDao;
import repository.OrderDao;
import ui.request.OrderCreateRequest;
import ui.request.OrderLineItemCreateRequest;
import ui.request.OrderStatusChangeRequest;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        menuDao = new InMemoryMenuDao();
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
    }

    @Nested
    class 주문_등록시 {

        @Test
        void 주문한_메뉴가_하나도_없다면_에외가_발생한다() {
            // given
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));
            final var order = new OrderCreateRequest(table.getId(), Collections.emptyList());
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문한_메뉴가_등록되지_않은_메뉴라면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = new OrderCreateRequest(table.getId(), List.of(new OrderLineItemCreateRequest(menu.getId(), 1L)));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void 주문에_사용된_테이블이_등록되있지_않으면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 1L);

            final var order = new OrderCreateRequest(1L, List.of(orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void 주문에_사용된_테이블이_비어있다면_예외가_발생한다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, true));

            final var order = new OrderCreateRequest(table.getId(), List.of(orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 테이블이_비어있지_않고_메뉴가_존재하면_정상적으로_등록된다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 1L);
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = new OrderCreateRequest(table.getId(), List.of(orderLineItem));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            // when
            final var savedOrder = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
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
            final var orderLineItem = new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice());
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = orderDao.save(new Order(1L, OrderStatus.COMPLETION, List.of(orderLineItem), null, table.getId()));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            final var nextOrder = new OrderStatusChangeRequest("MEAL");

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidOrderStatusException.class);
        }

        @Test
        void 식사중일_경우_조리중으로_변경할_수_없다() {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice());
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = orderDao.save(new Order(1L, OrderStatus.MEAL, List.of(orderLineItem), null, table.getId()));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            final var nextOrder = new OrderStatusChangeRequest("COOKING");

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidOrderStatusException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {"COOKING: MEAL", "MEAL:COMPLETION"}, delimiter = ':')
        void 변경_가능한_상태일경우_정상적으로_변경한다(OrderStatus previous, OrderStatus next) {
            // given
            final var menuGroup = new MenuGroup(1L, "메뉴 그룹");
            final var menu = menuDao.save(MenuFactory.createMenuOf("메뉴", BigDecimal.valueOf(0), menuGroup));
            final var orderLineItem = new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice());
            final var table = orderTableDao.save(OrderTableFactory.createOrderTableOf(0, false));

            final var order = orderDao.save(new Order(1L, previous, List.of(orderLineItem), null, table.getId()));
            final var orderService = new OrderService(menuDao, orderDao, orderTableDao);

            final var nextOrder = new OrderStatusChangeRequest(next.name());

            // when
            final var savedOrder = orderService.changeOrderStatus(order.getId(), nextOrder);

            // then
            assertThat(savedOrder.getOrderStatus()).isEqualTo(next.name());
        }
    }
}
