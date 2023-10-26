package kitchenpos.ordertable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(Collection<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
