package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderCreateRequest.getOrderLineItemCreateRequests();

        if (CollectionUtils.isEmpty(orderLineItemCreateRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemCreateRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, new ArrayList<>());


        for (final OrderLineItemCreateRequest orderLineItem : orderLineItemCreateRequests) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);

            order.addOrderLineItem(new OrderLineItem(order, menu, orderLineItem.getQuantity()));
        }

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(order);

        return savedOrder;
    }
}
