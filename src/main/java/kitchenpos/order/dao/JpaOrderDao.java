package kitchenpos.order.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.repository.JpaOrderLineItemRepository;
import kitchenpos.order.dao.repository.JpaOrderRepository;
import kitchenpos.order.domain.Order;

@Component
public class JpaOrderDao implements OrderDao {

    private final JpaOrderRepository orderRepository;
    private final JpaOrderLineItemRepository orderLineItemRepository;

    public JpaOrderDao(final JpaOrderRepository orderRepository,
        final JpaOrderLineItemRepository orderLineItemRepository) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Override
    public Order save(final Order entity) {
        Order savedOrder = orderRepository.save(entity);
        savedOrder.setIdToOrderLineItems();
        orderLineItemRepository.saveAll(savedOrder.getOrderLineItems());
        return savedOrder;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("order not found"));
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
        final List<String> orderStatuses) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
