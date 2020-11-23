package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import kitchenpos.exception.*;
import kitchenpos.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private TableGroup tableGroup;
    private OrderTable savedOrderTable;
    private Menu savedMenu1;
    private Menu savedMenu2;
    private Menu savedMenu3;
    private OrderCreateRequest.OrderLineItemDto orderLineItemDto1;
    private OrderCreateRequest.OrderLineItemDto orderLineItemDto2;
    private OrderCreateRequest.OrderLineItemDto orderLineItemDto3;
    private OrderCreateRequest.OrderLineItemDto orderLineItemDto4;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(NumberOfGuests.from(0), false);
        this.tableGroup = createSavedTableGroup(LocalDateTime.now(), Collections.singletonList(orderTable));
        this.savedOrderTable = createSavedOrderTable(this.tableGroup, NumberOfGuests.from(0), false);

        MenuGroup savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedMenu1 = createSavedMenu("양념간장두마리메뉴", MenuPrice.of(BigDecimal.valueOf(28_000), BigDecimal.valueOf(30_000)), savedMenuGroup);
        this.savedMenu2 = createSavedMenu("후라이드양념두마리메뉴", MenuPrice.of(BigDecimal.valueOf(27_000), BigDecimal.valueOf(30_000)), savedMenuGroup);
        this.savedMenu3 = createSavedMenu("후라이드간장두마리메뉴", MenuPrice.of(BigDecimal.valueOf(27_000), BigDecimal.valueOf(30_000)), savedMenuGroup);
        this.orderLineItemDto1 = new OrderCreateRequest.OrderLineItemDto(this.savedMenu1.getId(), 1);
        this.orderLineItemDto2 = new OrderCreateRequest.OrderLineItemDto(this.savedMenu2.getId(), 1);
        this.orderLineItemDto4 = new OrderCreateRequest.OrderLineItemDto(this.savedMenu3.getId(), 1);
        this.orderLineItemDto3 = new OrderCreateRequest.OrderLineItemDto(this.savedMenu2.getId(), 2);
    }

    @DisplayName("새로운 주문 생성")
    @Test
    void createOrderTest() {
        List<OrderCreateRequest.OrderLineItemDto> orderLineItemCreateRequests = Arrays.asList(this.orderLineItemDto1,
                                                                                              this.orderLineItemDto2);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(this.savedOrderTable.getId(),
                                                                       orderLineItemCreateRequests);

        OrderResponse orderResponse = this.orderService.createOrder(orderCreateRequest);

        assertAll(
                () -> assertThat(orderResponse).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(orderCreateRequest.getOrderTableId()),
                () -> assertThat(orderResponse.getOrderLineItemDtos()).hasSize(orderResponse.getOrderLineItemDtos().size()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목이 없으면 예외 발생")
    @Test
    void createOrderWithEmptyOrderLineItemsThenThrowException() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(this.savedOrderTable.getId(),
                                                                       Collections.emptyList());

        assertThatThrownBy(() -> this.orderService.createOrder(orderCreateRequest))
                .isInstanceOf(InvalidOrderLineItemDtosException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목 내에서 중복되는 메뉴가 있으면 예외 발생")
    @Test
    void createOrderWithOrderLineItemsCountNotEqualToMenuCountThenThrowException() {
        OrderCreateRequest.OrderLineItemDto orderLineItemDto1 =
                new OrderCreateRequest.OrderLineItemDto(this.savedMenu1.getId(), 1);
        OrderCreateRequest.OrderLineItemDto orderLineItemDto2 =
                new OrderCreateRequest.OrderLineItemDto(this.savedMenu1.getId(), 1);
        List<OrderCreateRequest.OrderLineItemDto> orderLineItemCreateRequests = Arrays.asList(orderLineItemDto1,
                                                                                              orderLineItemDto2);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(this.savedOrderTable.getId(),
                                                                       orderLineItemCreateRequests);

        assertThatThrownBy(() -> this.orderService.createOrder(orderCreateRequest)).isInstanceOf(InvalidOrderLineItemDtosException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 존재하지 않는 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;

        List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos = Arrays.asList(this.orderLineItemDto1, this.orderLineItemDto2);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(notExistOrderTableId, orderLineItemDtos);

        assertThatThrownBy(() -> this.orderService.createOrder(orderCreateRequest)).isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 빈 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithEmptyOrderTableThenThrowException() {
        OrderTable savedOrderTable = createSavedOrderTable(this.tableGroup, NumberOfGuests.from(0), true);
        List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos = Arrays.asList(this.orderLineItemDto1, this.orderLineItemDto2);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemDtos);

        assertThatThrownBy(() -> this.orderService.createOrder(orderCreateRequest)).isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("존재하는 모든 주문을 조회")
    @Test
    void listOrderTest() {
        List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos1 = Arrays.asList(this.orderLineItemDto1, this.orderLineItemDto2);
        OrderCreateRequest orderCreateRequest1 = new OrderCreateRequest(this.savedOrderTable.getId(), orderLineItemDtos1);

        List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos2 = Arrays.asList(this.orderLineItemDto3, this.orderLineItemDto4);
        OrderCreateRequest orderCreateRequest2 = new OrderCreateRequest(this.savedOrderTable.getId(), orderLineItemDtos2);

        List<OrderCreateRequest> orderCreateRequests = Arrays.asList(orderCreateRequest1, orderCreateRequest2);
        orderCreateRequests.forEach(order -> this.orderService.createOrder(order));

        List<OrderResponse> orderResponses = this.orderService.listAllOrders();

        assertThat(orderResponses).hasSize(orderCreateRequests.size());
    }

    @DisplayName("특정 주문의 주문 상태를 식사 혹은 계산 완료로 변경")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusTest(String orderStatus) {
        List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos = Arrays.asList(this.orderLineItemDto1, this.orderLineItemDto2);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(this.savedOrderTable.getId(), orderLineItemDtos);
        OrderResponse orderResponse = this.orderService.createOrder(orderCreateRequest);
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(orderStatus);

        OrderResponse changedOrderResponse = this.orderService.changeOrderStatus(orderResponse.getId(),
                                                                                 orderStatusChangeRequest);

        assertThat(changedOrderResponse.getOrderStatus()).isEqualTo(orderStatusChangeRequest.getOrderStatus());
    }

    @DisplayName("존재하지 않는 주문의 주문 상태를 변경하면 예외 발생")
    @Test
    void changeOrderStatusWithNotExistOrderThenThrowException() {
        Long notExistOrderId = -1L;
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(notExistOrderId, orderStatusChangeRequest))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @DisplayName("특정 주문의 주문 상태가 계산 완료일 때 주문 상태를 변경하면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatusWithOrderStatusIsCompletionThenThrowException(String orderStatus) {
        Order order = new Order(this.savedOrderTable, OrderStatus.COMPLETION, LocalDateTime.now());
        Order savedOrder = this.orderRepository.save(order);

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(orderStatus);

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(savedOrder.getId(), orderStatusChangeRequest))
                .isInstanceOf(OrderStatusNotChangeableException.class);
    }

    private TableGroup createSavedTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        return this.tableGroupRepository.save(tableGroup);
    }

    private OrderTable createSavedOrderTable(TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        orderTable.setTableGroup(tableGroup);
        return this.orderTableRepository.save(orderTable);
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);
        return this.menuGroupRepository.save(menuGroup);
    }

    private Menu createSavedMenu(String name, MenuPrice menuPrice, MenuGroup menuGroup) {
        Menu menu = new Menu(name, menuPrice, menuGroup);
        return this.menuRepository.save(menu);
    }
}