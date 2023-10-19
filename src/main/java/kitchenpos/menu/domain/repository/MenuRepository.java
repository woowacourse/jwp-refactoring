package kitchenpos.menu.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuPersistence;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.menu.persistence.MenuProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

  private final MenuDao menuDao;
  private final MenuProductDao menuProductDao;

  public MenuRepository(final MenuDao menuDao, final MenuProductDao menuProductDao) {
    this.menuDao = menuDao;
    this.menuProductDao = menuProductDao;
  }

  public Menu save(final Menu entity) {
    final MenuPersistence savedMenu = menuDao.save(MenuPersistence.from(entity));

    final List<MenuProduct> savedMenuProducts = saveMenuProducts(entity, savedMenu);
    return savedMenu.toMenu(savedMenuProducts);
  }

  private List<MenuProduct> saveMenuProducts(final Menu entity, final MenuPersistence savedMenu) {
    final List<MenuProduct> savedMenuProducts = new ArrayList<>();
    for (final MenuProduct menuProduct : entity.getMenuProducts()) {
      final MenuProduct savedMenuProduct = new MenuProduct(savedMenu.getId(),
          menuProduct.getProductId(), menuProduct.getQuantity());

      savedMenuProducts.add(menuProductDao.save(savedMenuProduct));
    }
    return savedMenuProducts;
  }

  public List<Menu> findAll() {
    return menuDao.findAll()
        .stream()
        .map(menu -> menu.toMenu(menuProductDao.findAllByMenuId(menu.getId())))
        .collect(Collectors.toList());
  }

}
