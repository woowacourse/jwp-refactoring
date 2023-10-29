package kitchenpos.order.persistence;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
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
    public List<OrderLineItem> saveAll(final List<OrderLineItem> orderLineItems) {
        return jpaOrderLineItemRepository.saveAll(orderLineItems);
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return jpaOrderLineItemRepository.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return jpaOrderLineItemRepository.findAll();
    }
}
