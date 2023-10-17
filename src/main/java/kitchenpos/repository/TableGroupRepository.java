package kitchenpos.repository;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupDao {

    @Override
    TableGroup save(TableGroup entity);

    @Override
    Optional<TableGroup> findById(final Long id);

    @Override
    List<TableGroup> findAll() ;

}
