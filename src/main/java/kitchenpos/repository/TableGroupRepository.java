package kitchenpos.repository;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long tableGroupId) {
        return this.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
