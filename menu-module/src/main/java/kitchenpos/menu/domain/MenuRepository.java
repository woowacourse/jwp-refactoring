package kitchenpos.menu.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findMenuById(final Long menuId) {
        return findById(menuId).orElseThrow(() -> new EmptyResultDataAccessException("메뉴 식별자값으로 메뉴를 조회할 수 없습니다.", 1));
    }

    @Query("select m " +
           "from Menu m " +
           "join fetch MenuGroup mg on mg.id = m.menuGroup.id ")
    List<Menu> joinMenuGroupAll();

    @Query("select m " +
           "from Menu m " +
           "where m.id in :menuIds")
    List<Menu> findInMenuIds(@Param("menuIds") final List<Long> menuIds);

    long countByIdIn(final List<Long> ids);
}
