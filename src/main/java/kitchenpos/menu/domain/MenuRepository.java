package kitchenpos.menu.domain;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    default Menu getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 메뉴를 찾을 수 없습니다.".formatted(id)));
    }

    @Query("select count(m) from Menu m where m.id in (:menuIds)")
    long countByIdIn(List<Long> menuIds);
}
