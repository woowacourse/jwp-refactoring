package kitchenpos.table.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.TableGroup;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
