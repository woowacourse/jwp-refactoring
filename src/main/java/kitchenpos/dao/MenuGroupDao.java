package kitchenpos.dao;

import kitchenpos.dto.MenuGroupDto;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao {
    MenuGroupDto save(MenuGroupDto entity);

    Optional<MenuGroupDto> findById(Long id);

    List<MenuGroupDto> findAll();

    boolean existsById(Long id);
}
