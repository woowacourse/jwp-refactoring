package kitchenpos.dao;

import kitchenpos.dto.TableGroupDto;

import java.util.List;
import java.util.Optional;

public interface TableGroupDao {
    TableGroupDto save(TableGroupDto entity);

    Optional<TableGroupDto> findById(Long id);

    List<TableGroupDto> findAll();
}
