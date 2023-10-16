package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.menu.persistence.MenuProductDao;
import kitchenpos.menu_group.persistence.MenuGroupDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

  private final MenuDao menuDao;
  private final MenuGroupDao menuGroupDao;
  private final MenuProductDao menuProductDao;
  private final ProductDao productDao;

  public MenuService(
      final MenuDao menuDao,
      final MenuGroupDao menuGroupDao,
      final MenuProductDao menuProductDao,
      final ProductDao productDao
  ) {
    this.menuDao = menuDao;
    this.menuGroupDao = menuGroupDao;
    this.menuProductDao = menuProductDao;
    this.productDao = productDao;
  }

  @Transactional
  public Menu create(final Menu menu) {
    final BigDecimal price = menu.getPrice();

    if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException();
    }

    if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
      throw new IllegalArgumentException();
    }

    final List<MenuProduct> menuProducts = menu.getMenuProducts();

    BigDecimal sum = BigDecimal.ZERO;
    for (final MenuProduct menuProduct : menuProducts) {
      final Product product = productDao.findById(menuProduct.getProductId())
          .orElseThrow(IllegalArgumentException::new);
      sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
    }

    if (price.compareTo(sum) > 0) {
      throw new IllegalArgumentException();
    }

    final Menu savedMenu = menuDao.save(menu);

    final Long menuId = savedMenu.getId();
    final List<MenuProduct> savedMenuProducts = new ArrayList<>();
    for (final MenuProduct menuProduct : menuProducts) {
      final MenuProduct savedMenuProduct = new MenuProduct(menuProduct.getSeq(), menuId,
          menuProduct.getProductId(), menuProduct.getQuantity());
      savedMenuProducts.add(menuProductDao.save(savedMenuProduct));
    }
    return new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
        savedMenu.getMenuGroupId(), savedMenuProducts);
  }

  public List<Menu> list() {
    final List<Menu> menus = menuDao.findAll();
    final List<Menu> savedMenuProducts = new ArrayList<>();
    for (final Menu menu : menus) {
      savedMenuProducts.add(
          new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
              menuProductDao.findAllByMenuId(menu.getId())));
    }
    return savedMenuProducts;
  }
}
