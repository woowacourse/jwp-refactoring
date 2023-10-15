package kitchenpos.application;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fake.InMemoryMenuRepository;
import kitchenpos.fake.InMemoryOrderDao;
import kitchenpos.fake.InMemoryOrderLineItemDao;
import kitchenpos.fake.InMemoryOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderFixture.orderRequest;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private MenuRepository menuRepository;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;
    private OrderTable savedOrderTable;
    private OrderLineItem savedOrderLineItem;
    private Menu menu;

    @BeforeEach
    void before() {
        menuRepository = new InMemoryMenuRepository();
        orderDao = new InMemoryOrderDao();
        orderLineItemDao = new InMemoryOrderLineItemDao();
        orderTableDao = new InMemoryOrderTableDao();
        orderService = new OrderService(menuRepository, orderDao, orderLineItemDao, orderTableDao);
        menu = menuRepository.save(menu("메뉴", 10000L, null, List.of()));
        savedOrderTable = orderTableDao.save(new OrderTable(null, 10, false));
        savedOrderLineItem = orderLineItemDao.save(new OrderLineItem(1L, menu.getId(), 10));
    }

    @Test
    void 주문_항목이_없다면_예외가_발생한다() {
        // given
        OrderRequest orderRequest = orderRequest(savedOrderTable.getId(), List.of());

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderRequest request = orderRequest(savedOrderTable.getId(), List.of(new OrderLineItemRequest(MAX_VALUE, 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderRequest request = orderRequest(MAX_VALUE, List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableDao.save(orderTable(10, true));
        OrderRequest orderRequest = orderRequest(orderTable.getId(), List.of(new OrderLineItemRequest(savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity())));

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderRequest order = orderRequest(savedOrderTable.getId(), List.of(new OrderLineItemRequest(savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity())));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING);
            softly.assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
            softly.assertThat(savedOrder.getOrderedTime()).isNotNull();
            softly.assertThat(savedOrder.getOrderLineItems()).map(OrderLineItem::getOrderId)
                    .isNotNull();
        });
    }

    @Test
    void 주문을_조회한다() {
        // given
        OrderTable orderTable = orderTableDao.save(orderTable(10, false));
        Order order1 = orderDao.save(order(orderTable.getId(), MEAL));
        OrderLineItem orderLineItem1 = orderLineItemDao.save(orderLineItem(order1.getId(), menu.getId(), 2L));
        order1.changeOrderLineItems(List.of(orderLineItem1));
        Order order2 = orderDao.save(order(orderTable.getId(), COOKING));
        OrderLineItem orderLineItem2 = orderLineItemDao.save(orderLineItem(order2.getId(), menu.getId(), 2L));
        order2.changeOrderLineItems(List.of(orderLineItem2));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .isEqualTo(List.of(order1, order2));
    }

    @Test
    void 주문의_상태를_변경할_때_주문이_존재하지_않으면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, new OrderChangeStatusRequest(COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경할_때_상태가_완료면_예외가_발생한다() {
        // given
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now());
        Order savedOrder = orderDao.save(order);
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(COOKING);

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        Order order = new Order(1L, COOKING, LocalDateTime.now());
        Order savedOrder = orderDao.save(order);
        OrderChangeStatusRequest newOrder = new OrderChangeStatusRequest(OrderStatus.MEAL);

        // when
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
            softly.assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId());
        });
    }
}
