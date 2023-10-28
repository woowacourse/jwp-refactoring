package kitchenpos.infra;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu getById(Long id);
}
