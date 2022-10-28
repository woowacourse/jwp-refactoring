package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends Repository<OrderTable, Long> {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
