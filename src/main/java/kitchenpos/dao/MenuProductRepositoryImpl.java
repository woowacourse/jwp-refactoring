package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct2;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.Product2;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepositoryImpl implements MenuProductRepository {

  private final MenuProductDao2 menuProductDao2;
  private final ProductDao2 productDao2;
  private final MenuDao2 menuDao2;
  private final MenuGroupDao2 menuGroupDao2;

  public MenuProductRepositoryImpl(
      final MenuProductDao2 menuProductDao2,
      final ProductDao2 productDao2,
      final MenuDao2 menuDao2,
      final MenuGroupDao2 menuGroupDao2
  ) {
    this.menuProductDao2 = menuProductDao2;
    this.productDao2 = productDao2;
    this.menuDao2 = menuDao2;
    this.menuGroupDao2 = menuGroupDao2;
  }

  @Override
  public MenuProduct2 save(final MenuProduct2 menuProduct2) {
    final MenuProductEntity menuProductEntity = menuProductDao2.save(
        new MenuProductEntity(
            menuProduct2.getMenu().getId(),
            menuProduct2.getProduct().getId(),
            menuProduct2.getQuantity())
    );

    return mapToMenuProductFrom(menuProductEntity);
  }

  private MenuProduct2 mapToMenuProductFrom(final MenuProductEntity menuProductEntity) {
    final Product2 product = productDao2.findById(menuProductEntity.getProductId())
        .map(it -> new Product2(
            it.getId(),
            it.getName(),
            it.getPrice()
        ))
        .orElseThrow(IllegalArgumentException::new);

    final MenuEntity menuEntity = menuDao2.findById(menuProductEntity.getMenuId())
        .orElseThrow(IllegalArgumentException::new);

    final MenuGroup menuGroup = menuGroupDao2.findById(menuEntity.getMenuGroupId())
        .map(it -> new MenuGroup(it.getId(), it.getName()))
        .orElseThrow(IllegalArgumentException::new);

    final Menu2 menu = new Menu2(
        menuEntity.getId(),
        menuEntity.getName(),
        menuEntity.getPrice(),
        menuGroup
    );

    return new MenuProduct2(
        menuProductEntity.getSeq(),
        menu,
        product,
        menuProductEntity.getQuantity()
    );
  }

  @Override
  public Optional<MenuProduct2> findById(final Long id) {
    final MenuProductEntity menuProductEntity = menuProductDao2.findById(id)
        .orElseThrow(IllegalArgumentException::new);

    return Optional.of(mapToMenuProductFrom(menuProductEntity));
  }

  @Override
  public List<MenuProduct2> findAll() {
    return menuProductDao2.findAll()
        .stream()
        .map(this::mapToMenuProductFrom)
        .collect(Collectors.toList());
  }

  @Override
  public List<MenuProduct2> findAllByMenuId(final Long menuId) {
    return menuProductDao2.findAllByMenuId(menuId)
        .stream()
        .map(this::mapToMenuProductFrom)
        .collect(Collectors.toList());
  }
}
