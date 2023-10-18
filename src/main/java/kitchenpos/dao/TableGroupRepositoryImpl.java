package kitchenpos.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroup2;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

  private final TableGroupDao2 tableGroupDao;

  public TableGroupRepositoryImpl(final TableGroupDao2 tableGroupDao) {
    this.tableGroupDao = tableGroupDao;
  }


  @Override
  public TableGroup2 save(final TableGroup2 tableGroup) {
    final TableGroupEntity entity = tableGroupDao.save(new TableGroupEntity(
        tableGroup.getCreatedDate()
    ));

    return new TableGroup2(
        entity.getId(),
        entity.getCreatedDate()
    );
  }

  @Override
  public Optional<TableGroup2> findById(final Long id) {
    return Optional.ofNullable(tableGroupDao.findById(id)
        .map(tableGroupEntity -> new TableGroup2(tableGroupEntity.getId(),
            tableGroupEntity.getCreatedDate()))
        .orElseThrow(IllegalArgumentException::new));
  }

  @Override
  public List<TableGroup2> findAll() {
    return tableGroupDao.findAll()
        .stream()
        .map(entity -> new TableGroup2(entity.getId(), entity.getCreatedDate()))
        .collect(Collectors.toList());
  }
}
