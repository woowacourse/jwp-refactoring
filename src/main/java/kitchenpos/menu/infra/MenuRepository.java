package kitchenpos.menu.infra;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu getById(Long id);
}
