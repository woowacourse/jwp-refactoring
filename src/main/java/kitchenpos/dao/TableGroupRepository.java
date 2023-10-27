package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TableGroupRepository extends Repository<TableGroup, Long> {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
