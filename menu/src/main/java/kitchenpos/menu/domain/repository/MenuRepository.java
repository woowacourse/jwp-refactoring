package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(final Long menuId) {
        return findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + menuId + "인 Menu를 찾을 수 없습니다!"));
    }

}
