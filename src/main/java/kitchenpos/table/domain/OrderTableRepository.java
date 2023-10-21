package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroup(final TableGroup tableGroup);

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);
}
