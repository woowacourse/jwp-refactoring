package kitchenpos.domain;

import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    @Override
    default MenuGroup getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 메뉴 그룹을 찾을 수 없습니다.".formatted(id)));
    }
}
