package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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