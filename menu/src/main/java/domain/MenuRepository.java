package domain;

import exception.MenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuException("해당하는 메뉴가 없습니다."));
    }
}
