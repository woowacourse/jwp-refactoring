package kitchenpos.dao;

import kitchenpos.dto.MenuDto;

import java.util.List;
import java.util.Optional;

public interface MenuDao {
    MenuDto save(MenuDto entity);

    Optional<MenuDto> findById(Long id);

    List<MenuDto> findAll();

    long countByIdIn(List<Long> ids);
}
