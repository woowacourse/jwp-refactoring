package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {
    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
