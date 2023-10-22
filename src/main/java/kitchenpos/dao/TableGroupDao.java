package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.springframework.data.repository.Repository;

public interface TableGroupDao extends Repository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
