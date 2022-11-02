package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query(value = "SELECT ot FROM OrderTable ot WHERE ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

}
