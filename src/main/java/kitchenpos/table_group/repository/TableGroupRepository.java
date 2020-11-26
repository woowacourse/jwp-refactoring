package kitchenpos.table_group.repository;

import kitchenpos.table_group.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

}
