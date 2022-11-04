package kitchenpos.menu.domain.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.Menu;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(final Menu menu);

    List<Menu> findAll();
}
