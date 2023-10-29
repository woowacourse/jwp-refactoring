package kitchenpos.table.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup findTableGroupByTableGroupId(final Long tableGroupId) {
        return findById(tableGroupId).orElseThrow(() -> new EmptyResultDataAccessException("단체 지정 식별자값으로 단체 지정을 조회할 수 없습니다.", 1));
    }
}
