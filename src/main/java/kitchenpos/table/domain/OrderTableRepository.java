package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    default OrderTable getById(final Long id) {
        return this.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
