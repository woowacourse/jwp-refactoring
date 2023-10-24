package kitchenpos.repository;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("SELECT ot FROM OrderTable ot WHERE ot.id IN (:orderTableIds)")
    List<OrderTable> findByIds(@Param("orderTableIds") List<Long> orderTableIds);
}
