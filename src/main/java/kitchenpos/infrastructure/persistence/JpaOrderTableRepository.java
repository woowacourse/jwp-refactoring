package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    List<OrderTable> findAllByIdIn(final List<Long> ids);

    boolean existsById(final long id);
}
