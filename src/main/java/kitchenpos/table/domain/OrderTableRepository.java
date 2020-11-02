package kitchenpos.table.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<Table, Long> {
    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroupId(Long tableGroupId);
}
