package kitchenpos.tablegroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.tablegroup.model.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
