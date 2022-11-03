package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.Menu;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
