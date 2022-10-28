package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.jpa.repository.JpaOrderLineItemRepository;
import kitchenpos.domain.OrderLineItem;

@Primary
@Repository
public class JpaOrderLineItemDao implements OrderLineItemDao {

    private final JpaOrderLineItemRepository orderLineItemRepository;

    public JpaOrderLineItemDao(JpaOrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return orderLineItemRepository.save(entity);
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return orderLineItemRepository.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItemRepository.findAll();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItemRepository.findByOrder(orderId);
    }

}
