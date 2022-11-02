package kitchenpos.menu.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.Menu;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(Menu entity);

    List<Menu> findAllByIdIn(List<Long> ids);

    List<Menu> findAll();

}
