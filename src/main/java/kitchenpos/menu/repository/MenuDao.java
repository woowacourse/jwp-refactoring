package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu, Long> {

}
