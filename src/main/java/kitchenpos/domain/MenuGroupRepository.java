package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 메뉴 그룹이 없습니다."));
    }

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
