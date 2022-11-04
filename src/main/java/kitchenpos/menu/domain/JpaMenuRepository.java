package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {
}
