package kitchenpos.table.infrastructure;

import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {

}
