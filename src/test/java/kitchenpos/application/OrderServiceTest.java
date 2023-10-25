package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create_order() {
        // given
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 2)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5));
        final Menu savedMenu = menuRepository.save(new Menu("메뉴", 10000, menuGroup.getId(), menuProducts));

        final OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(),
            List.of(new OrderLineItemRequest(savedMenu.getId(), 3)));

        // when
        final OrderResponse result = orderService.create(orderRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            softly.assertThat(result.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        });

        final List<OrderLineItemResponse> orderLineItems = result.getOrderLineItems();
        assertSoftly(softly -> {
            softly.assertThat(orderLineItems).hasSize(1);
            softly.assertThat(orderLineItems.get(0).getOrderId()).isEqualTo(result.getId());
        });
    }

    @DisplayName("주문 상품이 없으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_orderLineItem_empty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5));
        final List<OrderLineItemRequest> emptyOrderLineItemRequest = List.of();
        final OrderRequest wrongOrderRequest = new OrderRequest(savedOrderTable.getId(),
            emptyOrderLineItemRequest);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrderRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴없이 주문은 할 수 없습니다.");
    }

    @DisplayName("주문 상품의 개수와 주문 내역의 개수가 다르면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_wrong_orderLineItem_count() {
        // given
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 2)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5));
        final Menu savedMenu = menuRepository.save(new Menu("메뉴", 10000, menuGroup.getId(), menuProducts));
        final List<OrderLineItemRequest> duplicatedContainMenu = List.of(
            new OrderLineItemRequest(savedMenu.getId(), 3),
            new OrderLineItemRequest(savedMenu.getId(), 3));
        final OrderRequest wrongOrderRequest = new OrderRequest(savedOrderTable.getId(),
            duplicatedContainMenu);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrderRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않은 메뉴로 주문을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_not_found_orderTable() {
        // given
        final Long wrongOrderTableId = 0L;
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 2)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu savedMenu = menuRepository.save(new Menu("메뉴", 10000, menuGroup.getId(), menuProducts));

        final OrderRequest wrongOrderRequest = new OrderRequest(wrongOrderTableId,
            List.of(new OrderLineItemRequest(savedMenu.getId(), 3)));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrderRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_empty_orderTable() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5));
        savedOrderTable.changeEmptyStatus(true);
        final List<OrderLineItemRequest> emptyOrderLineItemRequest = List.of();
        final OrderRequest wrongOrderRequest = new OrderRequest(savedOrderTable.getId(),
            emptyOrderLineItemRequest);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrderRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴없이 주문은 할 수 없습니다.");
    }

    @DisplayName("전체 주문을 조회할 수 있다.")
    @Test
    void find_all_order() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(2, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, false));
        orderRepository.save(new Order(orderTable1.getId(), new OrderLineItems(List.of())));
        orderRepository.save(new Order(orderTable2.getId(), new OrderLineItems(List.of())));

        // when
        final List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change_order_status() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
        final Order order = new Order(orderTable.getId(), new OrderLineItems(List.of()));
        orderRepository.save(order);
        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());

        // when
        final OrderResponse result = orderService.changeOrderStatus(order.getId(), request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(order.getId());
            softly.assertThat(result.getOrderStatus()).isEqualTo(result.getOrderStatus());
        });
    }

    @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
    @Test
    void change_order_fail_with_not_found_order() {
        // given
        final Long wrongOrderId = 0L;
        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COOKING.name());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 주문입니다.");
    }

    @DisplayName("주문 상태가 COMPLETION이면 주문 상태를 변경할 수 없다.")
    @Test
    void change_order_fail_with_completion_order() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
        final Order order = new Order(orderTable.getId(), new OrderLineItems(List.of()));
        order.changeStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order);

        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COOKING.name());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 Completion인 상태인 경우 주문 테이블의 상태를 변경할 수 없습니다.");
    }
}
