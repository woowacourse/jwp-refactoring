package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("select o from OrderTable o where o.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
