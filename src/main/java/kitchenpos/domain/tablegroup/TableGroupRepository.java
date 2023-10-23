package kitchenpos.domain.tablegroup;

import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테이블 그룹입니다."));
    }
}
