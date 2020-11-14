package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @DisplayName("새로운 주문를 생성한다.")
    @Test
    void createTest() {
        // given
        OrderTable orderTable = createOrderTable(false);
        List<OrderLineItem> orderLineItem = createOrderLineItem();

        // when
        Order result = orderService.create(orderTable.getId(), orderLineItem);

        // then
        Order savedOrder = orderRepository.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.findAllByOrder(result);

        assertThat(savedOrder.getOrderTable().getId()).isNotNull();
        assertThat(savedOrder.getOrderTable().getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrderLineItems).hasSize(1);
    }

    @DisplayName("새로운 주문 생성 시, OrderLineItem이 없으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsExceptionTest() {
        assertThatThrownBy(() -> orderService.create(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableExceptionTest() {
        OrderTable orderTable = createOrderTable(true);
        List<OrderLineItem> orderLineItem = createOrderLineItem();

        assertThatThrownBy(() -> orderService.create(orderTable.getId(), orderLineItem))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 수량을 나타내는 orderLineItem는 menu를 중복으로 가지면 안된다.")
    @Test
    void changeOrderStateTest() {
        Menu menu = createMenu();
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(menu),
                new OrderLineItem(menu)
        );
        orderLineItemRepository.saveAll(orderLineItems);
        assertThatThrownBy(() -> orderService.create(1L, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 목록을 조회한다.")
    @Test
    void listTest() {
        // given
        OrderTable orderTable = createOrderTable(false);
        List<OrderLineItem> orderLineItems = createOrderLineItem();
        orderService.create(orderTable.getId(), orderLineItems);

        // when
        List<Order> list = orderService.findAllFetch();

        // when
        assertThat(list).hasSize(orderRepository.findAll().size());
        assertThat(list.get(0).getOrderLineItems()).hasSize(1);
    }

    @DisplayName("Order 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        final OrderStatus CHANGED_STATUS = OrderStatus.COMPLETION;

        // given
        OrderTable orderTable = createOrderTable(false);
        List<OrderLineItem> orderLineItem = createOrderLineItem();

        // when
        Order result = orderService.create(orderTable.getId(), orderLineItem);
        orderService.changeOrderStatus(result.getId(), CHANGED_STATUS.name());

        // then
        Order changeOrder = orderRepository.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        assertThat(changeOrder.getOrderStatus()).isEqualTo(CHANGED_STATUS);
    }

    @DisplayName("존재하지 않는 Order 상태로 변경할 수 없다.")
    @Test
    void changeOrderStatusExceptionTest() {
        final String CHANGED_STATUS = "NOT_VALID_STATUS";

        OrderTable orderTable = createOrderTable(false);
        List<OrderLineItem> orderLineItem = createOrderLineItem();

        Order result = orderService.create(orderTable.getId(), orderLineItem);
        assertThatThrownBy(() -> result.updateOrderStatus(CHANGED_STATUS))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable(10, empty);
        return orderTableRepository.save(orderTable);
    }

    private Menu createMenu() {
        Menu menu = new Menu("포테이토_피자", 1000L, menuGroupRepository.save(new MenuGroup("피자")));
        return menuRepository.save(menu);
    }

    private List<OrderLineItem> createOrderLineItem() {
        Menu menu = createMenu();
        return Collections.singletonList(orderLineItemRepository.save(new OrderLineItem(menu)));
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        orderLineItemRepository.deleteAll();
        productRepository.deleteAll();
        menuRepository.deleteAll();
        orderRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}