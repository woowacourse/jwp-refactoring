package kitchenpos.repository.order;

import kitchenpos.domain.order.TableGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long>, TableRepositoryCustom {
}
