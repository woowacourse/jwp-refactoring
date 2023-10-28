package kitchenpos.ordertablegroup.domain.repository;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableGroupRepository extends JpaRepository<OrderTableGroup, Long> {
    default OrderTableGroup getById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
