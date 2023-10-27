package kitchenpos.menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getBy(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("그런 메뉴는 없습니다"));
    }

    default void validateContains(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("그런 메뉴는 없습니다");
        }
    }
}
