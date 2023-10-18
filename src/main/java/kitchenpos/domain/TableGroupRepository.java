package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.TableGroupEntity;

public interface TableGroupRepository {

  TableGroup2 save(TableGroup2 tableGroup);

  Optional<TableGroup2> findById(Long id);

  List<TableGroup2> findAll();
}
