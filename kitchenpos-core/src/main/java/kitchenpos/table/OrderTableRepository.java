package kitchenpos.table;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query(value = "select * from order_table "
        + "where table_group_id is null "
        + "and id in :orderTableIds "
        , nativeQuery = true)
    List<OrderTable> findAllByIdInAndTableGroupIsNull(
        @Param("orderTableIds") List<Long> orderTableIds);
}
