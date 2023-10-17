package kitchenpos.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("SELECT o "
            + "FROM OrderTable o "
            + "WHERE o.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByGroupId(@Param("tableGroupId") final Long tableGroupId);
}
