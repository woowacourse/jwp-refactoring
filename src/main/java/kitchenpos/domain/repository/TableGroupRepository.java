package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;

public interface TableGroupRepository {

  TableGroup save(TableGroup tableGroup);

  Optional<TableGroup> findById(Long id);

  List<TableGroup> findAll();
}
