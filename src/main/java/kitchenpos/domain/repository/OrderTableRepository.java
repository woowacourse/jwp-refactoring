package kitchenpos.domain.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select ot from OrderTable ot left join fetch ot.tableGroup where ot.id =:orderTableId")
    Optional<OrderTable> findByIdWithTableGroup(@Param("orderTableId") Long orderTableId);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
