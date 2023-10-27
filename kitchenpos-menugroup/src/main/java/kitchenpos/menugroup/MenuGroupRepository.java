package kitchenpos.menugroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default void validateContains(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("그런 메뉴 그룹은 없습니다");
        }
    }
}
