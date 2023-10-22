package kitchenpos.tablegroup.persistence;

import kitchenpos.tablegroup.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

}
