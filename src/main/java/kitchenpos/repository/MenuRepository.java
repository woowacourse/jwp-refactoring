package kitchenpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Menu;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(Menu entity);

    List<Menu> findAllByIdIn(List<Long> ids);

    List<Menu> findAll();

}
