package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
