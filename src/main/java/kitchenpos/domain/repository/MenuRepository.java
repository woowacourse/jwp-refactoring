package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.entity.Menu;

public interface MenuRepository {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
