package kitchenpos.domain;

import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Override
    default TableGroup getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 테이블 그룹을 찾을 수 없습니다.".formatted(id)));
    }
}
