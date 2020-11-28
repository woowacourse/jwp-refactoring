package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderTableId IN (:ids)")
    List<Order> findAllByOrderTableIdIn(@Param("ids") List<Long> orderTableIds);

    List<Order> findAllByOrderTableId(Long orderTableId);
}
