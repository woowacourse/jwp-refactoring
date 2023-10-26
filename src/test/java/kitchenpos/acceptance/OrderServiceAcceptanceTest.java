package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;


    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = orderTableRepository.save(OrderTable.forSave(4, false, Collections.emptyList()));
        final Product product1 = productRepository.save(Product.forSave("후라이드", BigDecimal.TEN));
        final Product product2 = productRepository.save(Product.forSave("양념", BigDecimal.TEN));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("치킨"));
        final Menu menu = Menu.forSave("후라이드", List.of(MenuProduct.forSave(product1, 1L),
                                                       MenuProduct.forSave(product2, 1L)), menuGroup.getId());
        menuRepository.save(menu);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(
            new OrderLineItemCreateRequest(menu.getId(), 1L)
        ));

        // when
        final OrderResponse order = orderService.create(request);

        // then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 항목의 개수가 일치하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotSameOrderLineItemsCount() {
        // given
        final Product product1 = productRepository.save(Product.forSave("후라이드", BigDecimal.TEN));
        final Product product2 = productRepository.save(Product.forSave("양념", BigDecimal.TEN));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("치킨"));
        final Menu menu = Menu.forSave("후라이드", List.of(MenuProduct.forSave(product1, 1L),
                                                       MenuProduct.forSave(product2, 1L)), menuGroup.getId());
        menuRepository.save(menu);

        orderTableRepository.save(OrderTable.forSave(4, false, Collections.emptyList()));
        final OrderCreateRequest request = new OrderCreateRequest(1L, List.of(
            new OrderLineItemCreateRequest(menu.getId(), 1L),
            new OrderLineItemCreateRequest(Long.MAX_VALUE, 1L)
        ));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistOrderTable() {
        // given
        final Product product1 = productRepository.save(Product.forSave("후라이드", BigDecimal.TEN));
        final Product product2 = productRepository.save(Product.forSave("양념", BigDecimal.TEN));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("치킨"));
        final Menu menu = Menu.forSave("후라이드", List.of(MenuProduct.forSave(product1, 1L),
                                                       MenuProduct.forSave(product2, 1L)), menuGroup.getId());
        menuRepository.save(menu);

        orderTableRepository.save(OrderTable.forSave(4, false, Collections.emptyList()));
        final OrderCreateRequest request = new OrderCreateRequest(1L, List.of(
            new OrderLineItemCreateRequest(menu.getId(), 1L),
            new OrderLineItemCreateRequest(Long.MAX_VALUE, 1L)
        ));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        final Order order1 = createOrder();
        final Order order2 = createOrder();

        // when
        final List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(OrderResponse::getId)
            .containsExactlyInAnyOrder(order1.getId(), order2.getId());
    }

    private Order createOrder() {
        final OrderTable orderTable = orderTableRepository.save(OrderTable.forSave(4, false, Collections.emptyList()));
        final Product product1 = productRepository.save(Product.forSave("후라이드", BigDecimal.TEN));
        final Product product2 = productRepository.save(Product.forSave("양념", BigDecimal.TEN));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("치킨"));
        final Menu menu = Menu.forSave("후라이드", List.of(MenuProduct.forSave(product1, 1L),
                                                       MenuProduct.forSave(product2, 1L)), menuGroup.getId());
        menuRepository.save(menu);

        final OrderLineItem orderLineItem1 = OrderLineItem.forSave(1L, "치킨", new Price(BigDecimal.valueOf(20L)),
                                                                   menu);
        final OrderLineItem orderLineItem2 = OrderLineItem.forSave(2L, "치킨", new Price(BigDecimal.valueOf(40L)),
                                                                   menu);

        final Order order = Order.forSave(OrderStatus.COOKING, List.of(orderLineItem1, orderLineItem2), orderTable.getId());
        orderRepository.save(order);

        return order;
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = createOrder();

        // when
        final OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(),
                                                                          new OrderChangeOrderStatusRequest(
                                                                              "COMPLETION"));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("완료된 주문을 변경하려고 시도하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failCompletionOrder() {
        // given
        final OrderTable orderTable = orderTableRepository.save(OrderTable.forSave(4, false, Collections.emptyList()));
        final Product product1 = productRepository.save(Product.forSave("후라이드", BigDecimal.TEN));
        final Product product2 = productRepository.save(Product.forSave("양념", BigDecimal.TEN));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("치킨"));
        final Menu menu = Menu.forSave("후라이드", List.of(MenuProduct.forSave(product1, 1L),
                                                       MenuProduct.forSave(product2, 1L)), menuGroup.getId());
        menuRepository.save(menu);

        final OrderLineItem orderLineItem1 = OrderLineItem.forSave(1L, "치킨", new Price(BigDecimal.TEN), menu);
        final OrderLineItem orderLineItem2 = OrderLineItem.forSave(2L, "피자", new Price(BigDecimal.TEN), menu);

        final Order order = Order.forSave(OrderStatus.COMPLETION, List.of(orderLineItem1, orderLineItem2), orderTable.getId());
        orderRepository.save(order);

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(),
                                                                new OrderChangeOrderStatusRequest("COOKING")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
