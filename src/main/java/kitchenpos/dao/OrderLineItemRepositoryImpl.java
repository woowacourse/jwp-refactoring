package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct2;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.Product2;
import kitchenpos.domain.TableGroup2;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepositoryImpl implements OrderLineItemRepository {

  private final OrderLineItemDao2 orderLineItemDao;
  private final MenuGroupDao2 menuGroupDao;
  private final MenuDao2 menuDao;
  private final MenuProductDao2 menuProductDao;
  private final ProductDao2 productDao;

  public OrderLineItemRepositoryImpl(
      final OrderLineItemDao2 orderLineItemDao,
      final MenuGroupDao2 menuGroupDao,
      final MenuDao2 menuDao,
      final MenuProductDao2 menuProductDao,
      final ProductDao2 productDao
  ) {
    this.orderLineItemDao = orderLineItemDao;
    this.menuGroupDao = menuGroupDao;
    this.menuDao = menuDao;
    this.menuProductDao = menuProductDao;
    this.productDao = productDao;
  }

  @Override
  public OrderLineItem2 save(final OrderLineItem2 orderLineItem, final Order2 order) {
    final OrderLineItemEntity orderLineItemEntity = new OrderLineItemEntity(
        order.getId(),
        orderLineItem.getMenu().getId(),
        orderLineItem.getQuantity()
    );

    return mapToOrderLineItem(orderLineItemDao.save(orderLineItemEntity));
  }

  private OrderLineItem2 mapToOrderLineItem(final OrderLineItemEntity orderLineItemEntity) {
    return new OrderLineItem2(
        orderLineItemEntity.getSeq(),
        menuDao.findById(orderLineItemEntity.getMenuId())
            .map(this::mapToMenu)
            .orElseThrow(IllegalArgumentException::new),
        orderLineItemEntity.getQuantity()
    );
  }

  private Menu2 mapToMenu(final MenuEntity menuEntity) {
    return new Menu2(
        menuEntity.getId(),
        menuEntity.getName(),
        menuEntity.getPrice(),
        mapToMenuGroup(menuEntity),
        findMenuProducts(menuEntity)
    );
  }

  private List<MenuProduct2> findMenuProducts(final MenuEntity menuEntity) {
    return menuProductDao.findAllByMenuId(menuEntity.getId())
        .stream()
        .map(this::mapToMenuProduct)
        .collect(Collectors.toList());
  }

  private MenuProduct2 mapToMenuProduct(final MenuProductEntity menuProductEntity) {
    return new MenuProduct2(
        menuProductEntity.getSeq(),
        findProduct(menuProductEntity.getProductId()),
        menuProductEntity.getQuantity()
    );
  }

  private Product2 findProduct(final Long productId) {
    return productDao.findById(productId)
        .map(it2 -> new Product2(it2.getId(), it2.getName(), it2.getPrice()))
        .orElseThrow(IllegalArgumentException::new);
  }

  private MenuGroup mapToMenuGroup(final MenuEntity menuEntity) {
    return menuGroupDao.findById(menuEntity.getMenuGroupId())
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()))
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public Optional<OrderLineItem2> findById(final Long id) {
    return orderLineItemDao.findById(id)
        .map(this::mapToOrderLineItem);
  }

  @Override
  public List<OrderLineItem2> findAll() {
    return orderLineItemDao.findAll()
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderLineItem2> findAllByOrderId(final Long orderId) {
    return orderLineItemDao.findAllByOrderId(orderId)
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }
}
