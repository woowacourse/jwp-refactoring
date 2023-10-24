package kitchenpos.repository;

import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
