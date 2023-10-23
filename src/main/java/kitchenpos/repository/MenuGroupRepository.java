package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {

    List<MenuGroup> findAll();
}
