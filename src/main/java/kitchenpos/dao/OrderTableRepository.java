package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
