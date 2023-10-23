package kitchenpos.domain.table;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("select o from OrderTable o where o.tableGroupId = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
