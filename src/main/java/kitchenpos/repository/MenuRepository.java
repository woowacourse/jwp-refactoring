package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(final Long id) {
        return this.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
