package kitchenpos.order.infrastructure;


import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaOrderLineItemDao implements OrderLineItemDao {

    private JpaOrderLineItemRepository jpaOrderLineItemRepository;

    public JpaOrderLineItemDao(JpaOrderLineItemRepository jpaOrderLineItemRepository) {
        this.jpaOrderLineItemRepository = jpaOrderLineItemRepository;
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return jpaOrderLineItemRepository.save(entity);
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return jpaOrderLineItemRepository.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return jpaOrderLineItemRepository.findAll();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return jpaOrderLineItemRepository.findByOrder_id(orderId);
    }
}
