package kitchenpos.repository;

import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepository {
}
