package kitchenpos.repository;

import static kitchenpos.exception.TableGroupExceptionType.TABLE_GROUP_NOT_EXISTS;

import kitchenpos.domain.TableGroup;
import kitchenpos.exception.TableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_EXISTS));
    }
}
