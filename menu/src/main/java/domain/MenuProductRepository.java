package domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenu(Menu menu);

    List<MenuProduct> findAllByMenuIn(List<Menu> allMenus);
}
