package tablegroup.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import tablegroup.domain.TableGroup;

public interface TableGroupDao extends Repository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
