package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
