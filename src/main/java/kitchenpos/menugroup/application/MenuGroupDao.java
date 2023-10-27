package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.menugroup.application.domain.MenuGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupDao extends CrudRepository<MenuGroup, Long> {
    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
