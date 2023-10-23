package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.TableGroupEntity;

public interface TableGroupDao {
    TableGroupEntity save(TableGroupEntity entity);

    Optional<TableGroupEntity> findById(Long id);

    List<TableGroupEntity> findAll();
}
