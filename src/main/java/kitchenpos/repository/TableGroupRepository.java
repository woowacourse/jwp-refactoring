package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.TableGroup;
import org.springframework.data.repository.CrudRepository;

public interface TableGroupRepository extends CrudRepository<TableGroup, Long> {

    @Override
    List<TableGroup> findAll();
}
