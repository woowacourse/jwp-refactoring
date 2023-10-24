package kitchenpos.repository;

import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(Collection<Long> ids);

    @Query("select distinct ot from OrderTable ot join fetch ot.orders where ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupIdWithOrders(@Param("tableGroupId") Long tableGroupId);
}
