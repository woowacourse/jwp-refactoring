package kitchenpos.core.tablegroup.application;

import java.util.List;
import kitchenpos.core.tablegroup.domain.TableGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupDao extends CrudRepository<TableGroup, Long> {
    List<TableGroup> findAll();
}
