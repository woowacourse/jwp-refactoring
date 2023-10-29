package kitchenpos.table.domain.repository;

import kitchenpos.table.domain.model.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
    }
}
