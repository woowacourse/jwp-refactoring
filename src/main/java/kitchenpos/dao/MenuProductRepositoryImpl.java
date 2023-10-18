package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.domain.Menu;
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

  public MenuProductRepositoryImpl(
      final MenuProductDao2 menuProductDao2,
      final ProductDao2 productDao2
  ) {
    this.menuProductDao2 = menuProductDao2;
    this.productDao2 = productDao2;
  }

  @Override
  public MenuProduct2 save(final MenuProduct2 menuProduct2, final Menu2 menu) {
    final MenuProductEntity menuProductEntity = menuProductDao2.save(
        new MenuProductEntity(
            menu.getId(),
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

    return new MenuProduct2(
        menuProductEntity.getSeq(),
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
