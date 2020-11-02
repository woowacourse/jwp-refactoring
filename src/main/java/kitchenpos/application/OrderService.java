package kitchenpos.application;

import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orderz;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Orderz create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Orderz savedOrder = orderRepository.save(new Orderz(orderTableId, orderLineItems));
        OrderLineItems orderLineItems_ = new OrderLineItems(orderLineItems, savedOrder);
        orderLineItemRepository.saveAll(orderLineItems_.getOrderLineItems());

        return savedOrder;
    }

    public List<Orderz> list() {
        final List<Orderz> orders = orderRepository.findAll();

        for (final Orderz order : orders) {
            order.updateOrderLineItems(orderLineItemRepository.findAllByOrder(order));
        }

        return orders;
    }

    @Transactional
    public Orderz changeOrderStatus(Long orderId, String orderStatus) {
        final Orderz order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.updateOrderStatus(orderStatus);
        return order;
    }
}
