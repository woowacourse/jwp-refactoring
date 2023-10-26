package kitchenpos.table_group.repository;

import kitchenpos.table_group.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 테이블 그룹이 존재하지 않습니다."));
    }
}
