package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.support.fixture.domain.MenuFixture.getMenu;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.getMenuGroup;
import static kitchenpos.support.fixture.domain.OrderFixture.getOrder;
import static kitchenpos.support.fixture.domain.OrderLineItemFixture.getOrderLineItem;
import static kitchenpos.support.fixture.domain.OrderTableFixture.getOrderTable;
import static kitchenpos.support.fixture.dto.OrderCreateRequestFixture.orderCreateRequest;
import static kitchenpos.support.fixture.dto.OrderStatusChangeRequestFixture.orderStatusChangeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.ServiceTest;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
public class OrderServiceTest {

    private static final List<OrderLineItemRequest> EMPTY_ORDER_LINE_ITEM_REQUEST = Collections.emptyList();
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

    @Autowired
    private OrderLineItemDao orderLineItemDao;

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
            //when
            //then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest(1L, EMPTY_ORDER_LINE_ITEM_REQUEST)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴을_주문하면_예외를_던진다() {
            //given
            //when
            //then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest(1L, NOT_EXIST_MENU_ID, 1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블에서_주문을_하면_예외를_던진다() {
            //given
            //when
            //then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest(NOT_EXIST_TABLE_ID, savedMenu.getId(), 1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블에서_주문을_하면_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(true));

            //when
            //then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest(orderTable.getId(), savedMenu.getId(), 1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_성공하면_주문_상태가_조리중으로_변경된다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));

            //when
            final Order savedOrder = orderService.create(orderCreateRequest(orderTable.getId(), savedMenu.getId(), 1));

            //then
            final Order findOrder = orderDao.findById(savedOrder.getId()).get();
            assertThat(findOrder.getOrderStatus()).isEqualTo(COOKING);
        }
    }

    @Test
    void 모든_주문을_조회한다() {
        //given
        final OrderTable orderTable = orderTableDao.save(getOrderTable(false));

        final Order firstOrder = orderDao.save(getOrder(orderTable.getId(), COOKING));
        final Order secondOrder = orderDao.save(getOrder(orderTable.getId(), COOKING));

        final OrderLineItem firstOrderOrderLineItem =
                orderLineItemDao.save(getOrderLineItem(firstOrder.getId(), savedMenu.getId(), 1));
        final OrderLineItem secondOrderOrderLineItem =
                orderLineItemDao.save(getOrderLineItem(secondOrder.getId(), savedMenu.getId(), 1));

        firstOrder.registerOrderLineItems(new OrderLineItems(List.of(firstOrderOrderLineItem)));
        secondOrder.registerOrderLineItems(new OrderLineItems(List.of(secondOrderOrderLineItem)));

        //when
        final List<Order> orders = orderService.list();

        //then
        assertThat(orders)
                .usingRecursiveComparison()
                .isEqualTo(List.of(firstOrder, secondOrder));
    }

    @Nested
    class 주문의_상태를_변경할_때 {

        @Test
        void 존재하지_않는_주문이면_예외가_발생한다() {
            //given
            //when
            //then
            assertThatThrownBy(() ->
                    orderService.changeOrderStatus(NOT_EXIST_ORDER_ID, orderStatusChangeRequest(COOKING.name()))
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이면_예외가_발생한다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            final Order order = orderDao.save(getOrder(orderTable.getId(), COMPLETION));

            //when
            //then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(order.getId(), orderStatusChangeRequest(COOKING.name()))
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경에_성공한다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            final Order order = orderDao.save(getOrder(orderTable.getId(), COOKING));
            orderLineItemDao.save(getOrderLineItem(order.getId(), savedMenu.getId(), 1));

            //when
            final Order savedUpdatedOrder = orderService.changeOrderStatus(
                    order.getId(),
                    orderStatusChangeRequest(COMPLETION.name())
            );

            //then
            final Order result = orderDao.findById(savedUpdatedOrder.getId()).get();
            assertThat(result.getOrderStatus()).isEqualTo(COMPLETION);
        }
    }
}
