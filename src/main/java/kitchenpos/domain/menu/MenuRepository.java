package kitchenpos.domain.menu;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    default Menu getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메뉴입니다."));
    }
}
