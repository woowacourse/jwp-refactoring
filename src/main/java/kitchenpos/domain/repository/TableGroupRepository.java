package kitchenpos.domain.repository;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long tableGroupId) {
        return findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + tableGroupId + "인 TableGroup을 찾을 수 없습니다!"));
    }

}
