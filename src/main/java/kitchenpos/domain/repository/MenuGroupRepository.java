package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.repository.Repository;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
