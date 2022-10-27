package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupDao extends JpaRepository<MenuGroup, Long>, MenuGroupDao {
}
