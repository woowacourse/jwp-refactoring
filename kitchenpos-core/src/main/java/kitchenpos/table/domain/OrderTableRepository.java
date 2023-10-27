package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<kitchenpos.table.domain.OrderTable, Long> {
    List<kitchenpos.table.domain.OrderTable> findAllByIdIn(final List<Long> orderTableIds);
}
