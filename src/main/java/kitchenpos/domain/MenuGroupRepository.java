package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuGroupException("해당하는 메뉴 그룹이 없습니다."));
    }
}
