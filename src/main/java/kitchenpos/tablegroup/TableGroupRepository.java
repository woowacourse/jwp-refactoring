package kitchenpos.tablegroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getBy(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("그런 테이블 그룹은 없습니다"));
    }

    default void validateContains(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("그런 테이블 그룹은 없습니다");
        }
    }
}
