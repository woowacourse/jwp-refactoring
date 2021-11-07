package kitchenpos.repository;

import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o JOIN FETCH OrderTable ot WHERE ot.id = :orderTableId")
    List<Orders> findAllByOrderTableId(Long orderTableId);
}
