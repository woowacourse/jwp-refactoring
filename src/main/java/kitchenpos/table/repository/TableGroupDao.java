package kitchenpos.table.repository;

import kitchenpos.table.domain.TableGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
