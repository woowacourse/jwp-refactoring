package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu,Long> {
}
