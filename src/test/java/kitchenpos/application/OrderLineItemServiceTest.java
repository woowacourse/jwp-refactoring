package kitchenpos.application;

import kitchenpos.application.order.OrderLineItemService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.request.OrderLineItemRequest;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithoutId;
import static kitchenpos.fixture.OrderFixture.createOrderWithOrderTable;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemWithoutId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderLineItemServiceTest {

    @Autowired
    private OrderLineItemService orderLineItemService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("OrderLineItems create 정상 동작")
    @Test
    void createOrderLineItems() {
        OrderTable orderTable = orderTableRepository.save(createOrderTableWithoutId());
        Order order = orderRepository.save(createOrderWithOrderTable(orderTable));
        Menu savedMenu = menuRepository.save(createMenuWithoutId());
        Long quantity = 1L;
        OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(savedMenu.getId(), quantity);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest);

        List<OrderLineItem> actual = orderLineItemService.createOrderLineItems(order, orderLineItemRequests);
        OrderLineItem expected = createOrderLineItemWithoutId(order, savedMenu, quantity);

        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    private OrderLineItemRequest createOrderLineItemRequest(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}