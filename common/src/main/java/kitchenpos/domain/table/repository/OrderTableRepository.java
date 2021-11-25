package kitchenpos.domain.table.repository;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query("select ot from OrderTable ot where ot.id in(:orderTableIds)")
    List<OrderTable> findAllByIdIn(@Param("orderTableIds") List<Long> orderTableIds);

    @Query("select ot from OrderTable ot where ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);

}
