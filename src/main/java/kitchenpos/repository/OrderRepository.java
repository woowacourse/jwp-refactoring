package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    Order getById(Long id);

    @Query("SELECT o FROM Order o join fetch o.orderTable WHERE o.id = :id")
    Optional<Order> findByIdFetch(@Param("id") Long id);

}
