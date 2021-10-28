package kitchenpos.table.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.TableGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaTableGroupDao implements TableGroupDao {
    private JpaTableGroupRepository tableGroupRepository;

    public JpaTableGroupDao(JpaTableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        return tableGroupRepository.save(entity);
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return tableGroupRepository.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupRepository.findAll();
    }
}
