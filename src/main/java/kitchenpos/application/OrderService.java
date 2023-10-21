package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderException.EmptyOrderTableException;
import kitchenpos.domain.exception.OrderException.NotExistsMenuException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.getById(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        validateOrder(orderLineItems, orderTable);

        return orderRepository.save(Order.of(orderTable, orderLineItems));
    }

    private void validateOrder(final List<OrderLineItem> orderLineItems, final OrderTable orderTable) {
        validateOrderLineItems(orderLineItems);
        validateOrderTable(orderTable);
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotExistsMenuException();
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException(orderTable.getId());
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest orderStatusChangeRequest) {
        final Order savedOrder = orderRepository.getById(orderId);

        savedOrder.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());

        return savedOrder;
    }
}
