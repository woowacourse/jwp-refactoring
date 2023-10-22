package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupDao {

    @Override
    TableGroup save(TableGroup entity);

    @Override
    Optional<TableGroup> findById(Long id);

    @Override
    List<TableGroup> findAll();
}
