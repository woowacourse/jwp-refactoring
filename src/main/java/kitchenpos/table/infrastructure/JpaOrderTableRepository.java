package kitchenpos.table.infrastructure;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroup_id(Long tableGroupId);
}
