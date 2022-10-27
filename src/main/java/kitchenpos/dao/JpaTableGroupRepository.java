package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
