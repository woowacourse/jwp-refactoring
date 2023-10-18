package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct2;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product2;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuDao2 menuDao;
  private final MenuGroupDao2 menuGroupDao;
  private final MenuProductDao2 menuProductDao;
  private final ProductDao2 productDao;

  public MenuRepositoryImpl(final MenuDao2 menuDao, final MenuGroupDao2 menuGroupDao,
      final MenuProductDao2 menuProductDao, final ProductDao2 productDao) {
    this.menuDao = menuDao;
    this.menuGroupDao = menuGroupDao;
    this.menuProductDao = menuProductDao;
    this.productDao = productDao;
  }

  @Override
  public Menu2 save(final Menu2 menu) {
    final MenuEntity savedMenuEntity = menuDao.save(
        new MenuEntity(
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroup().getId()
        )
    );

    return mapToMenu(savedMenuEntity);
  }

  private Menu2 mapToMenu(final MenuEntity menuEntity) {
    return new Menu2(
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

  private List<MenuProduct2> findMenuProducts(final Long menuId) {
    return menuProductDao.findAllByMenuId(menuId)
        .stream()
        .map(this::mapToMenuProduct)
        .collect(Collectors.toList());
  }

  private MenuProduct2 mapToMenuProduct(final MenuProductEntity menuProductEntity) {
    return new MenuProduct2(
        menuProductEntity.getSeq(),
        findProduct(menuProductEntity),
        menuProductEntity.getQuantity()
    );
  }

  private Product2 findProduct(final MenuProductEntity menuProductEntity) {
    return productDao.findById(menuProductEntity.getProductId())
        .map(this::mapToProduct)
        .orElseThrow(IllegalArgumentException::new);
  }

  private Product2 mapToProduct(final ProductEntity productEntity) {
    return new Product2(
        productEntity.getId(),
        productEntity.getName(),
        productEntity.getPrice()
    );
  }

  @Override
  public Optional<Menu2> findById(final Long id) {
    return menuDao.findById(id)
        .map(this::mapToMenu);
  }

  @Override
  public List<Menu2> findAll() {
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
