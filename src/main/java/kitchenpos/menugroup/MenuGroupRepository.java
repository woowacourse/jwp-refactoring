package kitchenpos.menugroup;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
