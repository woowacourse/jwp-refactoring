package kitchenpos.domain.repository;

import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("select o from Orders o where o.orderTable.id = :id")
    List<Orders> findAllByOrderTableId(Long id);
}
