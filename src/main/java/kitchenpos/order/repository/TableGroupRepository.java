package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.order.domain.TableGroup;

public interface TableGroupRepository extends Repository<TableGroup, Long> {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();

}
