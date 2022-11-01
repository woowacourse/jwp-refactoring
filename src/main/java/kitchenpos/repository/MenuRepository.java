package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {
}

//    @Override
//    public Menu save(Menu entity) {
//        Menu menu = menuDao.save(entity);
//        Long menuId = menu.getId();
//
//        for (MenuProduct menuProduct : menu.getMenuProducts()) {
//            menuProduct.setMenuId(menuId);
//            menuProductDao.save(menuProduct);
//        }
//        return menu;
//    }
//
//    @Override
//    public Optional<Menu> findById(Long id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<Menu> findAll() {
//        List<Menu> menus = menuDao.findAll();
//
//        for (final Menu menu : menus) {
//            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
//        }
//        return menus;
//    }
//
//    @Override
//    public long countByIdIn(List<Long> ids) {
//        return 0;
//    }
