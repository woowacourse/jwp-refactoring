package kitchenpos.tablegroup.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TableGroupRepository extends CrudRepository<TableGroup, Long> {

    List<TableGroup> findAll();

}
