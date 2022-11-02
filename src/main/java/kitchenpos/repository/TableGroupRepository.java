package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.repository.entity.TableGroupEntityRepository;
import org.springframework.data.repository.Repository;

public interface TableGroupRepository extends Repository<TableGroup, Long>, TableGroupEntityRepository {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
