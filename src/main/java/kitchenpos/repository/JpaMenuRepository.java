package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long>, MenuRepository {
}
