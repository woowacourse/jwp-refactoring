package kitchenpos.menu.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    int countByIdIn(List<Long> menuIds);

    @Query("SELECT m FROM Menu m JOIN FETCH m.menuGroup")
    List<Menu> findAllWithMenuGroup();
}
