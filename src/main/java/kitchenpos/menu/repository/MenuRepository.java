package kitchenpos.menu.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    default List<Menu> getAllById(List<Long> menuIds) {
        return menuIds.stream()
                .map(id -> findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다. id : " + id)))
                .collect(Collectors.toUnmodifiableList());
    }
}
