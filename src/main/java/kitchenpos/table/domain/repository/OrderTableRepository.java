package kitchenpos.table.domain.repository;

import java.util.List;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

}
