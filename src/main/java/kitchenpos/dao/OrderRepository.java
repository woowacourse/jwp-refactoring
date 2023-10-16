package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTable.id = :orderTableId")
    Optional<Order> findByOrderTableId(Long orderTableId);

    @Query("select o from Order o where o.orderTable.id in :orderTableIds")
    List<Order> findAllByOrderTableIds(List<Long> orderTableIds);
}
