package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TableGroupRepository extends Repository<TableGroup, Long> {

    TableGroup save(final TableGroup tableGroup);

    void removeById(final Long id);

    Optional<TableGroup> findById(final Long id);

    @Query(value = "select count(table_group.id) > 0 from table_group " +
            "left join order_table on table_group.id = order_table.id " +
            "where :orderTableId = order_table.id", nativeQuery = true)
    boolean existsByOrderTableIn(@Param("orderTableId") final OrderTable orderTable);
}
