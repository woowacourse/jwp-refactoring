package kitchenpos.tablegroup.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.repository.Repository;

public interface TableGroupDao extends Repository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
