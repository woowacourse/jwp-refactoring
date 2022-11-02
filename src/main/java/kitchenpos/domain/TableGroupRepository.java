package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(Long id) throws IllegalArgumentException {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다. id = " + id));
    }
}
