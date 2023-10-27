package kitchenpos.domain;

import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    default Menu getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 메뉴를 찾을 수 없습니다.".formatted(id)));
    }
}
