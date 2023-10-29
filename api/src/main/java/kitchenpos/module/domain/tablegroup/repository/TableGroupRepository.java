package kitchenpos.module.domain.tablegroup.repository;

import kitchenpos.module.domain.tablegroup.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
