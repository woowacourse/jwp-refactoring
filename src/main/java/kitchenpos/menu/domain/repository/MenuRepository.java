package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(final Menu menu);

    List<Menu> findAll();
}
