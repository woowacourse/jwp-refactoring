package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.Table;

public interface OrderTableRepository extends JpaRepository<Table, Long> {
    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroupId(Long tableGroupId);
}
