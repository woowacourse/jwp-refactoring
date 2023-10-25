package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    @Query("SELECT ot.id, ot.tableGroup.id, ot.numberOfGuests, ot.empty FROM OrderTable ot WHERE ot.id IN (:ids)")
    List<OrderTable> findAllByIdIn();
}
