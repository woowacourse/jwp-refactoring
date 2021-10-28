package kitchenpos.menugroup.infrastructure;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
