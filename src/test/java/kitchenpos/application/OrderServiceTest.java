package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableRepository;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateInfo;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu savedMenu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Arrays.asList(new MenuProduct(savedProduct1.getId(), 2L), new MenuProduct(savedProduct2.getId(), 1L),
                new MenuProduct(savedProduct3.getId(), 1L))));

        OrderLineItemCreateInfo orderLineItem = new OrderLineItemCreateInfo(savedMenu.getId(), 2L);
        List<OrderLineItemCreateInfo> orderLineItems = Collections.singletonList(orderLineItem);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), orderLineItems);

        OrderResponse actual = orderService.create(orderCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(OrderResponse::getId).isNotNull(),
            () -> assertThat(actual.getOrderLineItems()).extracting(OrderLineItemResponse::getOrderId)
                .containsOnly(actual.getId()),
            () -> assertThat(actual).extracting(OrderResponse::getOrderStatus).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(actual).extracting(OrderResponse::getOrderedTime).isNotNull()
        );
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void findList() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu savedMenu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Arrays.asList(new MenuProduct(savedProduct1.getId(), 2L), new MenuProduct(savedProduct2.getId(), 1L),
                new MenuProduct(savedProduct3.getId(), 1L))));

        orderRepository.save(
            Order.place(orderTable.getId(), Collections.singletonList(new OrderLineItem(savedMenu.getId(), 2L))));

        List<OrderResponse> actual = orderService.list();

        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual).allSatisfy(it ->
                assertThat(it).extracting(OrderResponse::getOrderLineItems,
                    InstanceOfAssertFactories.list(OrderLineItemResponse.class))
                    .extracting(OrderLineItemResponse::getOrderId)
                    .containsOnly(it.getId()))
        );
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu savedMenu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Arrays.asList(new MenuProduct(savedProduct1.getId(), 2L), new MenuProduct(savedProduct2.getId(), 1L),
                new MenuProduct(savedProduct3.getId(), 1L))));

        Order order = orderRepository.save(
            Order.place(orderTable.getId(), Collections.singletonList(new OrderLineItem(savedMenu.getId(), 2L))));

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL);
        OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(),
            orderStatusChangeRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo(orderStatusChangeRequest.getOrderStatus());
    }

    @DisplayName("주문 상태를 수정할 시 해당되는 orderId가 없으면 예외 처리한다.")
    @Test
    void changeOrderStatusWithNotExistingOrderId() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.MEAL)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}