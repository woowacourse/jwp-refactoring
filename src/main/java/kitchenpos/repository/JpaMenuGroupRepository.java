package kitchenpos.repository;

import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.repository.Repository;

public interface JpaMenuGroupRepository extends Repository<MenuGroup, Long>, MenuGroupRepository {
}
