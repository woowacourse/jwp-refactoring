package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select count(ot) from OrderTable ot where ot.id in :orderTableIds")
    long countAllByIdIn(List<Long> orderTableIds);

    @Query("select ot from OrderTable ot where ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroup(Long tableGroupId);
}
