package kitchenpos.core.repository.table;

import java.util.List;
import kitchenpos.core.domain.table.TableGroup;
import org.springframework.data.repository.Repository;

public interface TableGroupRepository extends Repository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    List<TableGroup> findAll();
}
