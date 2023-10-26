package kitchenpos.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
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
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    private static final List<OrderLineItemRequest> EMPTY_ORDER_LINE_ITEM_REQUEST = Collections.emptyList();
    private static final long NOT_EXIST_MENU_ID = -1L;
    private static final long NOT_EXIST_TABLE_ID = -1L;
    private static final long NOT_EXIST_ORDER_ID = -1L;
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupRepository.save(getMenuGroup("menuGroup"));
        savedMenu = menuRepository.save(getMenu("menu", 100L, menuGroup.getId()));
    }

    @Nested
    class 주문할_때 {

        @Test
        void 주문할_메뉴가_없으면_예외를_던진다() {
            //given
            final OrderCreateRequest request = orderCreateRequest(1L, EMPTY_ORDER_LINE_ITEM_REQUEST);

            //when
            //then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴을_주문하면_예외를_던진다() {
            //given
            final OrderCreateRequest request = orderCreateRequest(1L, NOT_EXIST_MENU_ID, 1);

            //when
            //then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블에서_주문을_하면_예외를_던진다() {
            //given
            final OrderCreateRequest request = orderCreateRequest(NOT_EXIST_TABLE_ID, savedMenu.getId(), 1);

            //when
            //then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블에서_주문을_하면_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableRepository.save(getOrderTable(true));
            final OrderCreateRequest request = orderCreateRequest(orderTable.getId(), savedMenu.getId(), 1);

            //when
            //then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_성공하면_주문_상태가_조리중으로_변경된다() {
            //given
            final OrderTable orderTable = orderTableRepository.save(getOrderTable(false));

            //when
            final Order savedOrder = orderService.create(orderCreateRequest(orderTable.getId(), savedMenu.getId(), 1));

            //then
            final Order findOrder = orderRepository.findById(savedOrder.getId()).get();
            assertThat(findOrder.getOrderStatus()).isEqualTo(COOKING);
        }
    }

    @Test
    void 모든_주문을_조회한다() {
        //given
        final OrderTable orderTable = orderTableRepository.save(getOrderTable(false));

        final Order firstOrder = orderRepository.save(getOrder(orderTable.getId(), COOKING));
        final Order secondOrder = orderRepository.save(getOrder(orderTable.getId(), COOKING));

        final OrderLineItem firstOrderOrderLineItem =
                orderLineItemRepository.save(getOrderLineItem(firstOrder.getId(), savedMenu.getId(), 1));
        final OrderLineItem secondOrderOrderLineItem =
                orderLineItemRepository.save(getOrderLineItem(secondOrder.getId(), savedMenu.getId(), 1));

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
            final OrderStatusChangeRequest request = orderStatusChangeRequest(COOKING.name());

            //when
            //then
            assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ORDER_ID, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이면_예외가_발생한다() {
            //given
            final OrderTable orderTable = orderTableRepository.save(getOrderTable(false));
            final Order order = orderRepository.save(getOrder(orderTable.getId(), COMPLETION));
            final OrderStatusChangeRequest request = orderStatusChangeRequest(COOKING.name());

            //when
            //then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경에_성공한다() {
            //given
            final OrderTable orderTable = orderTableRepository.save(getOrderTable(false));
            final Order order = orderRepository.save(getOrder(orderTable.getId(), COOKING));
            orderLineItemRepository.save(getOrderLineItem(order.getId(), savedMenu.getId(), 1));

            //when
            final Order savedUpdatedOrder = orderService.changeOrderStatus(
                    order.getId(),
                    orderStatusChangeRequest(COMPLETION.name())
            );

            //then
            final Order result = orderRepository.findById(savedUpdatedOrder.getId()).get();
            assertThat(result.getOrderStatus()).isEqualTo(COMPLETION);
        }
    }
}
