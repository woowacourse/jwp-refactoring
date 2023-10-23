package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.dao.mapper.MenuGroupMapper;
import kitchenpos.dao.mapper.MenuMapper;
import kitchenpos.dao.mapper.ProductMapper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuDao menuDao;
  private final MenuGroupDao menuGroupDao;
  private final MenuProductDao menuProductDao;
  private final ProductDao productDao;

  public MenuRepositoryImpl(
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

  @Override
  public Menu save(final Menu menu) {
    final MenuEntity savedMenuEntity = menuDao.save(
        new MenuEntity(
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroup().getId()
        )
    );

    List<MenuProduct> savedMenuProducts = savedMenuProducts(menu, savedMenuEntity);

    return MenuMapper.mapToMenu(
        savedMenuEntity,
        menu.getMenuGroup(),
        savedMenuProducts
    );
  }

  private List<MenuProduct> savedMenuProducts(final Menu menu, final MenuEntity savedMenuEntity) {
    List<MenuProduct> savedMenuProducts = new ArrayList<>();

    for (final MenuProduct menuProduct : menu.getMenuProducts()) {
      final MenuProductEntity savedEntity = saveMenuProduct(savedMenuEntity, menuProduct);

      savedMenuProducts.add(
          MenuMapper.mapToMenuProduct(savedEntity, menuProduct.getProduct())
      );
    }

    return savedMenuProducts;
  }

  private MenuProductEntity saveMenuProduct(
      final MenuEntity savedMenuEntity,
      final MenuProduct menuProduct
  ) {
    return menuProductDao.save(
        new MenuProductEntity(
            savedMenuEntity.getId(),
            menuProduct.getProduct().getId(),
            menuProduct.getQuantity()
        ));
  }

  @Override
  public Optional<Menu> findById(final Long id) {
    return menuDao.findById(id)
        .map(entity -> MenuMapper.mapToMenu(
            entity,
            findMenuGroup(entity.getMenuGroupId()),
            findMenuProducts(entity.getId())
        ));
  }

  private MenuGroup findMenuGroup(final Long menuGroupId) {
    return menuGroupDao.findById(menuGroupId)
        .map(MenuGroupMapper::mapToMenuGroup)
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<MenuProduct> findMenuProducts(final Long menuId) {
    return menuProductDao.findAllByMenuId(menuId)
        .stream()
        .map(entity -> MenuMapper.mapToMenuProduct(entity, findProduct(entity)))
        .collect(Collectors.toList());
  }

  private Product findProduct(final MenuProductEntity menuProductEntity) {
    return productDao.findById(menuProductEntity.getProductId())
        .map(ProductMapper::mapToProduct)
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public List<Menu> findAll() {
    return menuDao.findAll()
        .stream()
        .map(entity -> MenuMapper.mapToMenu(
            entity,
            findMenuGroup(entity.getMenuGroupId()),
            findMenuProducts(entity.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public long countByIdIn(final List<Long> ids) {
    return menuDao.countByIdIn(ids);
  }
}
