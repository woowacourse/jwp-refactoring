package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select o from OrderTable o where tableGroupId = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
