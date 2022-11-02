package kitchenpos.order.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.TableGroup;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
