package kitchenpos.core.domain.tablegroup.repository;

import kitchenpos.core.domain.tablegroup.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
