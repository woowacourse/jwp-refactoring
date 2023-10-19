package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuDao menuDao;
  private final MenuGroupDao menuGroupDao;
  private final MenuProductDao menuProductDao;
  private final ProductDao productDao;

  public MenuRepositoryImpl(final MenuDao menuDao, final MenuGroupDao menuGroupDao,
      final MenuProductDao menuProductDao, final ProductDao productDao) {
    this.menuDao = menuDao;
    this.menuGroupDao = menuGroupDao;
    this.menuProductDao = menuProductDao;
    this.productDao = productDao;
  }

  @Override
  public Menu save(final Menu menu) {
    final MenuEntity savedMenuEntity = menuDao.save(
        new MenuEntity(
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroup().getId()
        )
    );

    List<MenuProduct> savedMenuProducts = new ArrayList<>();

    for (final MenuProduct menuProduct : menu.getMenuProducts()) {
      final MenuProductEntity savedEntity = menuProductDao.save(
          new MenuProductEntity(
              savedMenuEntity.getId(),
              menuProduct.getProduct().getId(),
              menuProduct.getQuantity()
          ));

      savedMenuProducts.add(
          new MenuProduct(savedEntity.getSeq(), menuProduct.getProduct(), savedEntity.getQuantity())
      );
    }

    return new Menu(
        savedMenuEntity.getId(),
        savedMenuEntity.getName(),
        savedMenuEntity.getPrice(),
        menu.getMenuGroup(),
        savedMenuProducts
    );
  }

  private Menu mapToMenu(final MenuEntity menuEntity) {
    return new Menu(
        menuEntity.getId(),
        menuEntity.getName(),
        menuEntity.getPrice(),
        findMenuGroup(menuEntity.getMenuGroupId()),
        findMenuProducts(menuEntity.getId())
    );
  }

  private MenuGroup findMenuGroup(final Long menuGroupId) {
    return menuGroupDao.findById(menuGroupId)
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()))
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<MenuProduct> findMenuProducts(final Long menuId) {
    return menuProductDao.findAllByMenuId(menuId)
        .stream()
        .map(this::mapToMenuProduct)
        .collect(Collectors.toList());
  }

  private MenuProduct mapToMenuProduct(final MenuProductEntity menuProductEntity) {
    return new MenuProduct(
        menuProductEntity.getSeq(),
        findProduct(menuProductEntity),
        menuProductEntity.getQuantity()
    );
  }

  private Product findProduct(final MenuProductEntity menuProductEntity) {
    return productDao.findById(menuProductEntity.getProductId())
        .map(this::mapToProduct)
        .orElseThrow(IllegalArgumentException::new);
  }

  private Product mapToProduct(final ProductEntity productEntity) {
    return new Product(
        productEntity.getId(),
        productEntity.getName(),
        productEntity.getPrice()
    );
  }

  @Override
  public Optional<Menu> findById(final Long id) {
    return menuDao.findById(id)
        .map(this::mapToMenu);
  }

  @Override
  public List<Menu> findAll() {
    return menuDao.findAll()
        .stream()
        .map(this::mapToMenu)
        .collect(Collectors.toList());
  }

  @Override
  public long countByIdIn(final List<Long> ids) {
    return menuDao.countByIdIn(ids);
  }
}
