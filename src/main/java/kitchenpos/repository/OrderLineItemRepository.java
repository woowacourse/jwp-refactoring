package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long>, OrderLineItemDao {

    @Override
    OrderLineItem save(OrderLineItem entity);

    @Override
    Optional<OrderLineItem> findById(Long id);

    @Override
    List<OrderLineItem> findAll();

    @Override
    List<OrderLineItem> findAllByOrderId(Long orderId);

}
