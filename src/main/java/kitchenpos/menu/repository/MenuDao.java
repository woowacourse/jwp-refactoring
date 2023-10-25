package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.repository.Repository;

public interface MenuDao extends Repository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();
}
