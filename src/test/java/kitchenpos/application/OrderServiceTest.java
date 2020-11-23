package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithId;
import static kitchenpos.fixture.MenuFixture.createMenuWithoutId;
import static kitchenpos.fixture.OrderFixture.createOrderWithOrderTable;
import static kitchenpos.fixture.OrderFixture.createOrderWithOrderTableAndOrderStatus;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemWithOrderAndMenu;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;
import static kitchenpos.utils.TestUtils.findById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @DisplayName("Order 등록 성공")
    @Test
    void create() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithoutId());
        Menu savedMenu = menuRepository.save(createMenuWithoutId());
        OrderRequest orderRequest = createOrderRequest(savedOrderTable, savedMenu);

        OrderResponse actual = orderService.create(orderRequest);
        Order foundOrder = findById(orderRepository, actual.getId());
        Long orderLineItemId = actual.getOrderLineItemResponses().get(0).getSeq();
        OrderLineItem foundOrderLineItem = findById(orderLineItemRepository, orderLineItemId);
        OrderResponse expected = OrderResponse.of(foundOrder, Arrays.asList(foundOrderLineItem));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("Order에 속하는 OrderLineItem이 아무것도 없는 경우 예외 반환")
    @Test
    void createEmptyOrderLineItem() {
        OrderRequest orderRequestEmptyOrderLineItem = createOrderRequestEmptyOrderLineItem();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequestEmptyOrderLineItem))
                .withMessage("주문 내역이 비어있습니다.");
    }

    @DisplayName("Order를 받은 OrderTable이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTable() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithEmpty(true));
        OrderRequest orderRequestWithEmptyTable = createOrderRequest(savedOrderTable, createMenuWithId(1L));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequestWithEmptyTable))
                .withMessage("비어있는 order table에 order를 생성할 수 없습니다.");
    }

    @DisplayName("Order 전체 조회")
    @Test
    void list() {
        OrderTable orderTable = orderTableRepository.save(createOrderTableWithoutId());
        Order order = orderRepository.save(createOrderWithOrderTable(orderTable));
        Menu menu = menuRepository.save(createMenuWithoutId());
        OrderLineItem orderLineItem = orderLineItemRepository.save(createOrderLineItemWithOrderAndMenu(order, menu));

        List<OrderResponse> actual = orderService.list();
        OrderResponse expected = OrderResponse.of(order, Arrays.asList(orderLineItem));

        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("Order 상태 바꾸기 성공")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = orderTableRepository.save(createOrderTableWithoutId());
        Order savedOrder = orderRepository.save(createOrderWithOrderTableAndOrderStatus(orderTable, OrderStatus.MEAL));
        Order expect = createOrderWithOrderTableAndOrderStatus(orderTable, OrderStatus.COMPLETION);

        Order actual = orderService.changeOrderStatus(savedOrder.getId(), expect);

        assertThat(actual.getOrderStatus()).isEqualTo(expect.getOrderStatus());
    }

    private OrderRequest createOrderRequest(OrderTable orderTable, Menu menu) {
        return new OrderRequest(orderTable.getId(), Arrays.asList(new OrderLineItemRequest(menu.getId(), 1L)));
    }

    private OrderRequest createOrderRequestEmptyOrderLineItem() {
        return new OrderRequest(null, null);
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAll();
        menuRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
    }
}
