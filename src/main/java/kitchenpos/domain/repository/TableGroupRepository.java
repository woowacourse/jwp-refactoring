package kitchenpos.domain.repository;

import kitchenpos.domain.tableGroup.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
