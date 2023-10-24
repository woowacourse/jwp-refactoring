package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.OrderLineItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderLineItemRepositoryImpl implements OrderLineItemRepository {

    private final JpaOrderLineItemRepository jpaOrderLineItemRepository;

    public OrderLineItemRepositoryImpl(final JpaOrderLineItemRepository jpaOrderLineItemRepository) {
        this.jpaOrderLineItemRepository = jpaOrderLineItemRepository;
    }

    @Override
    public OrderLineItem save(final OrderLineItem orderLineItem) {
        return jpaOrderLineItemRepository.save(orderLineItem);
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return jpaOrderLineItemRepository.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return jpaOrderLineItemRepository.findAll();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return jpaOrderLineItemRepository.findAllByOrderId(orderId);
    }
}
