package kitchenpos.tablegroup.infra;

import java.util.NoSuchElementException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("테이블 그룹이 존재하지 않습니다."));
    }

    void delete(TableGroup tableGroup);
}
