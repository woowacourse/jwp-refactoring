package kitchenpos.domain.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query("select ot from OrderTable ot where ot.tableGroup.id in(:orderTableIds)")
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    @Query("select ot from OrderTable ot where ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

}
