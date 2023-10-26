package kitchenpos.listener;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenuProduct;
import kitchenpos.order.dto.CreatedOrderEvent;
import kitchenpos.order.dto.OrderLineItemsDto;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderMenuProductRepository;
import kitchenpos.order.repository.OrderMenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreatedOrderEventListener {

    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderMenuProductRepository orderMenuProductRepository;
    private final MenuRepository menuRepository;

    public CreatedOrderEventListener(OrderLineItemRepository orderLineItemRepository,
                                     OrderMenuRepository orderMenuRepository,
                                     OrderMenuProductRepository orderMenuProductRepository,
                                     MenuRepository menuRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.orderMenuProductRepository = orderMenuProductRepository;
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void listenCreatedOrderEvent(final CreatedOrderEvent event) {
        final List<Long> menuIds = event.getOrderLineItemsDtos().stream()
                .map(dto -> dto.getMenuId())
                .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        for (final OrderLineItemsDto dto : event.getOrderLineItemsDtos()) {
            final Menu menu = findMenuById(menus, dto.getMenuId());
            final OrderMenu orderMenu = orderMenuRepository.save(OrderMenu.from(menu));
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
                .map(menuProduct -> OrderMenuProduct.of(menuProduct))
                .collect(Collectors.toList());
        orderMenuProductRepository.saveAll(orderMenuProducts);
    }
}
