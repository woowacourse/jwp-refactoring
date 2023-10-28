package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderLineItemsDto;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.repository.OrderMenuProductRepository;
import kitchenpos.repository.OrderMenuRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = "kitchenpos.menu")
public class OrderSnapShotCreator {

    private final SnapShotCreator snapShotCreator;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderMenuProductRepository orderMenuProductRepository;

    public OrderSnapShotCreator(
            final SnapShotCreator snapShotCreator,
            final OrderMenuRepository orderMenuRepository,
            final OrderMenuProductRepository orderMenuProductRepository
    ) {
        this.snapShotCreator = snapShotCreator;
        this.orderMenuRepository = orderMenuRepository;
        this.orderMenuProductRepository = orderMenuProductRepository;
    }

    public void createOrderSnapShot(final List<OrderLineItemsDto> orderLineItemsDtos) {
        final List<Long> menuIds = orderLineItemsDtos.stream()
                .map(dto -> dto.getMenuId())
                .collect(Collectors.toList());
        final List<Menu> menus = snapShotCreator.findAllByIds(menuIds);
        for (final OrderLineItemsDto dto : orderLineItemsDtos) {
            final Menu menu = findMenuById(menus, dto.getMenuId());
            final OrderMenu orderMenu = orderMenuRepository.save(
                    new OrderMenu(
                            menu.getName(),
                            menu.getPrice(),
                            menu.getMenuGroupId()
                    )
            );
            orderMenuRepository.save(orderMenu);
            saveAllProduct(menu.getMenuProducts());
            dto.changeToOrderMenuId(orderMenu.getId());
        }
    }

    private Menu findMenuById(final List<Menu> menus, final Long id) {
        return menus.stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 메뉴가 없습니다."));
    }

    private void saveAllProduct(final List<MenuProduct> menuProducts) {
        final List<OrderMenuProduct> orderMenuProducts = menuProducts.stream()
                .map(menuProduct -> new OrderMenuProduct(
                        menuProduct.getMenuId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                        )
                )
                .collect(Collectors.toList());
        orderMenuProductRepository.saveAll(orderMenuProducts);
    }
}
