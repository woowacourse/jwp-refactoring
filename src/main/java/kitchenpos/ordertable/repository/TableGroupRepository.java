package kitchenpos.ordertable.repository;

import kitchenpos.ordertable.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
