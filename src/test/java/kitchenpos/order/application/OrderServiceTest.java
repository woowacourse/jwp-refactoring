package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugruop.domain.MenuGroup;
import kitchenpos.menugruop.domain.repository.MenuGroupRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private Menu menu;

    @Override
    public void setObject() {
        Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("1번 메뉴 그룹"));
        menu = menuRepository.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        final OrderTable newOrderTable = new OrderTable(4, false);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                createOrderLineItemRequest(menu.getId()));

        final OrderResponse response = orderService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(response.getOrderTableId()).isNotNull()
        );
    }

    @DisplayName("주문 등록 시 주문항목의 메뉴에 등록되어 있지 않은 주문 항목이 있으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderLineItem() {
        final OrderTable newOrderTable = new OrderTable(4, false);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                createInvalidOrderLineItemRequest());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문에서의 주문 테이블이 존재하지 않는 주문 테이블일 경우 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        final OrderCreateRequest request = new OrderCreateRequest(9999L, createOrderLineItemRequest());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문에서의 주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTable() {
        final OrderTable newOrderTable = new OrderTable(0, true);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                createOrderLineItemRequest());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 조회할 수 있다.")
    @Test
    void list() {
        final OrderTable newOrderTable = new OrderTable(4, false);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final Order newOrder = new Order(orderTable.getId(), "COOKING", LocalDateTime.now(),
                createOrderLineItem(menu.getId()));
        final Order order = orderRepository.save(newOrder);
        orderLineItemRepository.save(
                new OrderLineItem(order.getId(), menu.getId(), menu.getName(), menu.getPrice(), 10));
        final List<OrderResponse> response = orderService.list();

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1L),
                () -> assertThat(response.get(0).getOrderLineItems()).isNotEmpty()
        );
    }

    @DisplayName("주문의 상태를 수정할 수 있다.")
    @Test
    void changeOrderStatus() {
        final OrderTable newOrderTable = new OrderTable(4, false);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final Order newOrder = new Order(orderTable.getId(), "COOKING", LocalDateTime.now(),
                createOrderLineItem(menu.getId()));
        final Order order = orderRepository.save(newOrder);
        final OrderUpdateRequest request = new OrderUpdateRequest("MEAL");

        orderService.changeOrderStatus(order.getId(), request);
        final Order foundOrder = orderRepository.findById(order.getId()).get();

        assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 수정 시 존재하지 않는 주문일 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithInvalidOrder() {
        final OrderUpdateRequest request = new OrderUpdateRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(9999L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 수정 시 주문 상태가 계산 완료인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithCompletionOrderStatus() {
        final OrderTable newOrderTable = new OrderTable(4, false);
        final OrderTable orderTable = orderTableRepository.save(newOrderTable);
        final Order newOrder = new Order(orderTable.getId(), "COOKING", LocalDateTime.now(),
                createOrderLineItem(menu.getId()));
        final Order order = orderRepository.save(newOrder);
        final OrderUpdateRequest request = new OrderUpdateRequest("COMPLETION");

        orderService.changeOrderStatus(order.getId(), request);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderLineItemRequest> createInvalidOrderLineItemRequest() {
        final List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest(9999L, 10L));
        return orderLineItems;
    }

    private OrderLineItems createOrderLineItem(final Long... menuIds) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItem(menuId, 10));
        }
        return new OrderLineItems(orderLineItems);
    }

    private List<OrderLineItemRequest> createOrderLineItemRequest(final Long... menuIds) {
        final List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        for (final Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItemRequest(menuId, 10L));
        }
        return orderLineItems;
    }

    private MenuProducts createMenuProducts(final Long... productIds) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L, BigDecimal.valueOf(10000)));
        }
        return new MenuProducts(menuProducts, BigDecimal.valueOf(9000));
    }
}
