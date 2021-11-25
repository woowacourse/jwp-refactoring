package kitchenpos.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {

}
