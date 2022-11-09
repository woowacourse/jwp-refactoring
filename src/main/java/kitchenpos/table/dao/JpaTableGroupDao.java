package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.table.dao.repository.JpaTableGroupRepository;
import kitchenpos.table.domain.TableGroup;

@Component
public class JpaTableGroupDao implements TableGroupDao {

    private final JpaTableGroupRepository tableGroupRepository;

    public JpaTableGroupDao(final JpaTableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        return tableGroupRepository.save(entity);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroupRepository.findById(id);
    }

    @Override
    public TableGroup getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("table group not found"));
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupRepository.findAll();
    }
}
