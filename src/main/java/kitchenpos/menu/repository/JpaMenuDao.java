package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuDao extends JpaRepository<Menu, Long>, MenuDao {
}
