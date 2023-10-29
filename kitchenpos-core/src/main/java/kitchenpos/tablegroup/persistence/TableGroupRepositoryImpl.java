package kitchenpos.tablegroup.persistence;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final JpaTableGroupRepository jpaTableGroupRepository;

    public TableGroupRepositoryImpl(final JpaTableGroupRepository jpaTableGroupRepository) {
        this.jpaTableGroupRepository = jpaTableGroupRepository;
    }

    @Override
    public TableGroup save(final TableGroup tableGroup) {
        return jpaTableGroupRepository.save(tableGroup);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return jpaTableGroupRepository.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        return jpaTableGroupRepository.findAll();
    }
}
