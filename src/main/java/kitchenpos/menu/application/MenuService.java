package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.MenuProductQueryResponse;
import kitchenpos.menu.application.dto.MenuQueryResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.menu.persistence.MenuProductDao;
import kitchenpos.menu_group.persistence.MenuGroupDao;
import kitchenpos.product.domain.Price;
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
  public MenuQueryResponse create(final MenuCreateRequest request) {
    final Price price = new Price(request.getPrice());

    if (price.isNull() || price.isLessThan(Price.ZERO)) {
      throw new IllegalArgumentException();
    }

    if (!menuGroupDao.existsById(request.getMenuGroupId())) {
      throw new IllegalArgumentException();
    }

    final List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();

    Price totalPrice = Price.ZERO;
    for (final MenuProductCreateRequest menuProduct : menuProducts) {
      final Product product = productDao.findById(menuProduct.getProductId())
          .orElseThrow(IllegalArgumentException::new).toProduct();
      totalPrice = totalPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
    }

    if (price.isGreaterThan(totalPrice)) {
      throw new IllegalArgumentException();
    }

    final Menu savedMenu = menuDao.save(request.toMenu());

    final Long menuId = savedMenu.getId();
    final List<MenuProductQueryResponse> savedMenuProducts = new ArrayList<>();
    for (final MenuProductCreateRequest menuProduct : menuProducts) {
      final MenuProduct savedMenuProduct = new MenuProduct(menuId,
          menuProduct.getProductId(), menuProduct.getQuantity());
      savedMenuProducts.add(MenuProductQueryResponse.from(menuProductDao.save(savedMenuProduct)));
    }
    return new MenuQueryResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
        savedMenu.getMenuGroupId(), savedMenuProducts);
  }

  public List<MenuQueryResponse> list() {
    final List<Menu> menus = menuDao.findAll();
    final List<MenuQueryResponse> savedMenus = new ArrayList<>();
    for (final Menu menu : menus) {
      final List<MenuProductQueryResponse> savedMenuProducts = menuProductDao.findAllByMenuId(
              menu.getId())
          .stream()
          .map(MenuProductQueryResponse::from)
          .collect(Collectors.toList());
      savedMenus.add(
          new MenuQueryResponse(menu.getId(), menu.getName(), menu.getPrice(),
              menu.getMenuGroupId(), savedMenuProducts
          ));
    }
    return savedMenus;
  }
}
