package kitchenpos.repository;

import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupRepository {
}
