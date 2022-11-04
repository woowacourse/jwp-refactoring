package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuDao extends JpaRepository<Menu, Long>, MenuDao {
}
