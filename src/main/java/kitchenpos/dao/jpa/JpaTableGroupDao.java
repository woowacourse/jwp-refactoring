package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.jpa.repository.JpaTableGroupRepository;
import kitchenpos.domain.TableGroup;

@Primary
@Repository
public class JpaTableGroupDao implements TableGroupDao {

    private final JpaTableGroupRepository tableGroupRepository;

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
