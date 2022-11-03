package kitchenpos.ordertable.domain.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroup(@Param("tableGroupId") Long tableGroupId);
}
