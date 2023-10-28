package kitchenpos.menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(final Long menuId){
        return findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴가 존재하지 않습니다."));
    }
}
