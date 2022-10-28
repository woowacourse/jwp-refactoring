package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.Menu;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu menu);

    List<Menu> findAll();

    Optional<Menu> findById(Long menuId);
}
