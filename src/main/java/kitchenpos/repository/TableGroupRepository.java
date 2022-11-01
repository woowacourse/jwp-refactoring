package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.notfound.TableGroupNotFoundException;
import org.springframework.data.repository.Repository;

public interface TableGroupRepository extends Repository<TableGroup, Long> {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new TableGroupNotFoundException(id));
    }

    List<TableGroup> findAll();
}
