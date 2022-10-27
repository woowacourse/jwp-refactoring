package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.CrudRepository;

public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {
}
