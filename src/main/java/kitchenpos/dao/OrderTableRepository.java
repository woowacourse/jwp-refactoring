package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("select ot from OrderTable ot where ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroup(@Param("tableGroupId") Long tableGroupId);
}
