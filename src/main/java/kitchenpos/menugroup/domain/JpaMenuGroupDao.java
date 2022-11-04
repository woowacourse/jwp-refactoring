package kitchenpos.menugroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupDao extends JpaRepository<MenuGroup, Long>, MenuGroupDao {
}
