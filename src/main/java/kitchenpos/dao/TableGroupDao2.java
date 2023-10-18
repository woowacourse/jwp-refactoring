package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.TableGroup;

public interface TableGroupDao2 {
    TableGroupEntity save(TableGroupEntity entity);

    Optional<TableGroupEntity> findById(Long id);

    List<TableGroupEntity> findAll();
}
