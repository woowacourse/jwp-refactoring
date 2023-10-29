package kitchenpos.tablegroup.persistence;

import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {

    TableGroup save(final TableGroup tableGroup);

    Optional<TableGroup> findById(final Long id);

    List<TableGroup> findAll();
}
