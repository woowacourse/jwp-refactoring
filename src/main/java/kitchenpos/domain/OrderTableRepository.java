package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroup(@Param("tableGroupId") Long tableGroupId);
}
