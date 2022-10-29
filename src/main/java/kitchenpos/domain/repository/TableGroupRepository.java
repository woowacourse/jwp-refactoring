package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
