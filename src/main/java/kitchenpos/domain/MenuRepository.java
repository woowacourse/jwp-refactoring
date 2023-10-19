package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 메뉴가 없습니다."));
    };

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
