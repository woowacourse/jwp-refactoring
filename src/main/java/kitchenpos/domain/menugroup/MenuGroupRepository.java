package kitchenpos.domain.menugroup;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();
}
