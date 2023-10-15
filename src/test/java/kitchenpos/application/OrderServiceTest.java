package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.support.fixture.MenuFixture.getMenu;
import static kitchenpos.support.fixture.MenuGroupFixture.getMenuGroup;
import static kitchenpos.support.fixture.OrderFixture.getOrder;
import static kitchenpos.support.fixture.OrderLineItemFixture.getOrderLineItem;
import static kitchenpos.support.fixture.OrderTableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
public class OrderServiceTest {

    private static final List<OrderLineItem> EMPTY_ORDER_LINE_ITEMS = Collections.emptyList();
    private static final long NOT_EXIST_MENU_ID = -1L;
    private static final long NOT_EXIST_TABLE_ID = -1L;
    private static final long NOT_EXIST_ORDER_ID = -1L;
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup("menuGroup"));
        savedMenu = menuDao.save(getMenu("menu", 100L, menuGroup.getId()));
    }

    @Nested
    class 주문할_때 {

        @Test
        void 주문할_메뉴가_없으면_예외를_던진다() {
            //given
            final Order order = getOrder(1L, EMPTY_ORDER_LINE_ITEMS);

            //when
            //then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴을_주문하면_예외를_던진다() {
            //given
            final OrderLineItem orderLineItem = getOrderLineItem(null, NOT_EXIST_MENU_ID, 1L);
            final Order order = getOrder(1L, List.of(orderLineItem));

            //when
            //then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블에서_주문을_하면_예외를_던진다() {
            //given
            final OrderLineItem orderLineItem = getOrderLineItem(null, savedMenu.getId(), 1L);
            final Order order = getOrder(NOT_EXIST_TABLE_ID, List.of(orderLineItem));

            //when
            //then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블에서_주문을_하면_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(true));
            final OrderLineItem orderLineItem = getOrderLineItem(null, savedMenu.getId(), 1L);
            final Order order = getOrder(orderTable.getId(), List.of(orderLineItem));

            //when
            //then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_성공하면_주문_상태가_조리중으로_변경된다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            final OrderLineItem orderLineItem = getOrderLineItem(null, savedMenu.getId(), 1L);
            final Order order = getOrder(orderTable.getId(), List.of(orderLineItem));

            //when
            final Order savedOrder = orderService.create(order);

            //then
            final Order findOrder = orderDao.findById(savedOrder.getId()).get();
            assertThat(findOrder.getOrderStatus()).isEqualTo(COOKING.name());
        }
    }

    @Test
    void 모든_주문을_조회한다() {
        //given
        final OrderTable orderTable = orderTableDao.save(getOrderTable(false));

        final Order order1 = orderDao.save(getOrder(orderTable.getId(), COOKING));
        final Order order2 = orderDao.save(getOrder(orderTable.getId(), COOKING));
        order1.setOrderLineItems(EMPTY_ORDER_LINE_ITEMS);
        order2.setOrderLineItems(EMPTY_ORDER_LINE_ITEMS);

        //when
        final List<Order> orders = orderService.list();

        //then
        assertThat(orders)
            .usingRecursiveComparison()
            .isEqualTo(List.of(order1, order2));
    }

    @Nested
    class 주문의_상태를_변경할_때 {

        @Test
        void 존재하지_않는_주문이면_예외가_발생한다() {
            //given
            //when
            //then
            assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ORDER_ID, getOrder(1L, COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이면_예외가_발생한다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            final Order order = orderDao.save(getOrder(orderTable.getId(), COMPLETION));
            final Order updatedOrder = getOrder(null, COOKING);

            //when
            //then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), updatedOrder))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경에_성공한다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            final Order order = orderDao.save(getOrder(orderTable.getId(), COOKING));
            final Order updatedOrder = getOrder(null, COMPLETION);

            //when
            final Order savedUpdatedOrder = orderService.changeOrderStatus(order.getId(), updatedOrder);

            //then
            final Order result = orderDao.findById(savedUpdatedOrder.getId()).get();
            assertThat(result.getOrderStatus()).isEqualTo(COMPLETION.name());
        }
    }
}
