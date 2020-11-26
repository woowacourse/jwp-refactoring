package kitchenpos.tablegroup.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Override
    List<TableGroup> findAll();
}
