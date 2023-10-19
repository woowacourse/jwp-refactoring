package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findMenuById(final Long menuId) {
        return findById(menuId).orElseThrow(() -> new EmptyResultDataAccessException("메뉴 식별자값으로 메뉴를 조회할 수 없습니다.", 1));
    }

    @Query("select m " +
           "from Menu m " +
           "join fetch MenuGroup mg on mg.id = m.menuGroup.id ")
    List<Menu> joinMenuGroupAll();

    long countByIdIn(final List<Long> ids);
}
