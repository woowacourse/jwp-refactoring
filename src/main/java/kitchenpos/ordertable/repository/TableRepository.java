package kitchenpos.ordertable.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    boolean existsByIdAndAndEmptyIsFalse(Long id);
}
