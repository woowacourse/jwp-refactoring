package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TableGroupRepository extends CrudRepository<TableGroup, Long> {

    @Override
    List<TableGroup> findAll();
}
