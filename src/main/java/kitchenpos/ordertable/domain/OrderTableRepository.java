package kitchenpos.ordertable.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("SELECT ot FROM OrderTable ot WHERE ot.id IN :ids")
    List<OrderTable> findAllByIdIn(final List<Long> ids);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}
