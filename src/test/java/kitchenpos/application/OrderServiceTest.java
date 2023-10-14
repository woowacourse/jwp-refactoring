package kitchenpos.application;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fake.InMemoryMenuDao;
import kitchenpos.fake.InMemoryOrderDao;
import kitchenpos.fake.InMemoryOrderLineItemDao;
import kitchenpos.fake.InMemoryOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;
    private OrderTable savedOrderTable;
    private OrderLineItem savedOrderLineItem;

    @BeforeEach
    void before() {
        menuDao = new InMemoryMenuDao();
        orderDao = new InMemoryOrderDao();
        orderLineItemDao = new InMemoryOrderLineItemDao();
        orderTableDao = new InMemoryOrderTableDao();
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Menu menu = menuDao.save(new Menu("메뉴", BigDecimal.valueOf(10000), null, List.of()));
        savedOrderTable = orderTableDao.save(new OrderTable(null, 10, false));
        savedOrderLineItem = orderLineItemDao.save(new OrderLineItem());
        savedOrderLineItem.setMenuId(menu.getId());
    }

    @Test
    void 주문_항목이_없다면_예외가_발생한다() {
        // given
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of());

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(MAX_VALUE, 1L);
        OrderRequest order = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(MAX_VALUE, 1L);
        OrderRequest order = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 10, true));
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(new OrderLineItemRequest(savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity())));

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderRequest order = new OrderRequest(savedOrderTable.getId(), List.of(new OrderLineItemRequest(savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity())));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softly.assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
            softly.assertThat(savedOrder.getOrderedTime()).isNotNull();
            softly.assertThat(savedOrder.getOrderLineItems()).map(OrderLineItem::getOrderId)
                    .isNotNull();
        });
    }

    @Test
    void 주문을_조회한다() {
        // given
        OrderRequest order = new OrderRequest(savedOrderTable.getId(), List.of(new OrderLineItemRequest(savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity())));
        Order savedOrder = orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(savedOrder));
    }

    @Test
    void 주문의_상태를_변경할_때_주문이_존재하지_않으면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, new OrderChangeStatusRequest(OrderStatus.COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경할_때_상태가_완료면_예외가_발생한다() {
        // given
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now());
        Order savedOrder = orderDao.save(order);
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COOKING);

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now());
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
