package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderCreateRequestDto;
import kitchenpos.dto.OrderLineCreateRequestDto;
import kitchenpos.dto.OrderResponseDto;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);

        OrderResponseDto orderResponse = orderService.create(orderCreateRequestDto);

        assertThat(orderResponse.getId()).isNotNull();
    }

    @DisplayName("존재하지 않는 테이블은 주문할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_WithNonExistingTable_ThrownException(Long tableId) {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(tableId, orderLineCreateRequests);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 주문할 수 없다.")
    @Test
    void create_WithEmptyTable_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록은 하나 이상이어야 한다.")
    @Test
    void create_WithZeroOrderList_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            Collections.EMPTY_LIST);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴로는 주문할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create__ThrownException(Long menuId) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menuId, 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);
        orderService.create(orderCreateRequest);

        List<OrderResponseDto> orderResponses = orderService.list();

        assertThat(orderResponses).hasSize(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String status) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);
        OrderResponseDto orderResponse = orderService.create(orderCreateRequest);

        Order changedOrderRequest = createOrder(orderResponse.getId(), orderResponse.getOrderTableId(), status,
            LocalDateTime.now(), Collections.EMPTY_LIST);

        Order changedOrder = orderService.changeOrderStatus(changedOrderRequest.getId(),
            changedOrderRequest);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(status);
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_WithCompletionOrder_ThrownException(String status) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
            Collections.singletonList(menuProductRequest)));
        List<OrderLineCreateRequestDto> orderLineCreateRequests = Collections.singletonList(
            new OrderLineCreateRequestDto(menu.getId(), 1));
        OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto(orderTable.getId(),
            orderLineCreateRequests);
        OrderResponseDto orderResponse = orderService.create(orderCreateRequest);

        Order completionOrderRequest = createOrder(orderResponse.getId(), orderResponse.getOrderTableId(), "COMPLETION",
            LocalDateTime.now(), Collections.EMPTY_LIST);
        orderService.changeOrderStatus(completionOrderRequest.getId(), completionOrderRequest);

        Order changedOrderRequest = createOrder(orderResponse.getId(), orderResponse.getOrderTableId(), status,
            LocalDateTime.now(), Collections.EMPTY_LIST);

        assertThatThrownBy(
            () -> orderService.changeOrderStatus(changedOrderRequest.getId(), changedOrderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}