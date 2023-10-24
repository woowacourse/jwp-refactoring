package kitchenpos.dao;

import kitchenpos.dto.MenuProductDto;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao {
    MenuProductDto save(MenuProductDto entity);

    Optional<MenuProductDto> findById(Long id);

    List<MenuProductDto> findAll();

    List<MenuProductDto> findAllByMenuId(Long menuId);
}
