package kitchenpos.table.repository;

import kitchenpos.table.domain.entity.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
