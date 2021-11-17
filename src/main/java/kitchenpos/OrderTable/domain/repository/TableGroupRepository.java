package kitchenpos.OrderTable.domain.repository;

import kitchenpos.OrderTable.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
