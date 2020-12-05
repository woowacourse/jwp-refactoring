package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));
        Product product = productRepository.save(new Product("간장치킨", 10000L));
        Menu menu = menuRepository.save(new Menu("간장 치킨 두마리", 19000L, menuGroup));
        menuProductRepository.save(new MenuProduct(menu, product, 2L));

        //when
        OrderResponse orderResponse = orderService.create(new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(1L, menu.getId()))));

        //then
        Order findOrder = orderRepository.findById(orderResponse.getOrderId())
                .orElseThrow(RuntimeException::new);
        List<OrderLineItem> findOrderLineItems = orderLineItemRepository.findAllByOrderId(findOrder.getId());

        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(findOrderLineItems).hasSize(1);
    }

    @DisplayName("존재하지 않는 테이블에 주문을 할 수 없다.")
    @Test
    void createException2() {
        Long notExistOrderTableId = -1L;
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));
        Product product = productRepository.save(new Product("간장치킨", 10000L));
        Menu menu = menuRepository.save(new Menu("간장 치킨 두마리", 19000L, menuGroup));
        menuProductRepository.save(new MenuProduct(menu, product, 2L));

        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(notExistOrderTableId,
                Collections.singletonList(new OrderLineItemCreateRequest(1L, menu.getId())))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블에 주문을 할 수 없습니다.");
    }

    @DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
    @Test
    void createException3() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));
        Product product = productRepository.save(new Product("간장치킨", 10000L));
        Menu menu = menuRepository.save(new Menu("간장 치킨 두마리", 19000L, menuGroup));
        menuProductRepository.save(new MenuProduct(menu, product, 2L));

        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(1L, menu.getId())))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 메뉴로 주문을 할 수 없다.")
    @Test
    void createException4() {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));

        //when
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(1L, -1L)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다.");
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));
        Menu menu = menuRepository.save(new Menu("간장 치킨 두마리", 19000L, menuGroup));

        Order order = orderRepository.save(new Order());
        orderLineItemRepository.save(new OrderLineItem(1L, order, menu));

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        Order order = orderRepository.save(new Order());

        //when
        orderService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(orderStatus.name()));

        //then
        Order changedOrder = orderRepository.findById(order.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void changeOrderStatusException1(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        Order order = orderRepository.save(new Order(OrderStatus.COMPLETION));

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(orderStatus.name())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 주문을 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, new OrderStatusChangeRequest(OrderStatus.COMPLETION.name())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문을 변경할 수 없습니다.");
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
        productRepository.deleteAll();
    }
}