package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuException("해당하는 메뉴가 없습니다."));
    }
}
