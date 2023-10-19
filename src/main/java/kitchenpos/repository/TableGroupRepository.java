package kitchenpos.repository;

import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.TableGroupException.NotExistsTableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long tableGroupId) {
        return findById(tableGroupId)
                .orElseThrow(() -> new NotExistsTableGroupException(tableGroupId));
    }
}
