package kitchenpos.order.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public interface TableGroupRepository extends Repository<TableGroup, Long> {

    TableGroup save(final TableGroup tableGroup);

    void removeById(final Long id);

    Optional<TableGroup> findById(final Long id);

    @Query(value = "select count(table_group.id) > 0 from table_group " +
            "left join order_table on table_group.id = order_table.id " +
            "where :orderTableId = order_table.id", nativeQuery = true)
    boolean existsByOrderTableIn(@Param("orderTableId") final OrderTable orderTable);
}
