package kitchenpos.menu.infra;

import java.util.NoSuchElementException;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("메뉴가 존재하지 않습니다."));
    }

    @Override
    MenuGroup getReferenceById(Long aLong);
}
