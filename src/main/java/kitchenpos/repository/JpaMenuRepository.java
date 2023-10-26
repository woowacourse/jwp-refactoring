package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    Menu getById(Long id);
}
