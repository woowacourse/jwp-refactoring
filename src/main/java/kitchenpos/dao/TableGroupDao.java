package kitchenpos.dao;

import kitchenpos.domain.TableGroup;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupDao extends CrudRepository<TableGroup, Long> {
    List<TableGroup> findAll();
}
