package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses);
}
