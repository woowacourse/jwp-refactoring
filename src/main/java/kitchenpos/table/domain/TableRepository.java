package kitchenpos.table.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroupId(Long tableGroupId);
}
