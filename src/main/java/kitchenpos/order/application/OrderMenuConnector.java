package kitchenpos.order.application;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.repository.OrderMenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuConnector {
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    public OrderMenuConnector(MenuRepository menuRepository, OrderMenuRepository orderMenuRepository) {
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
    }

    public void validateMenuId(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new BadRequestException(ErrorType.MENU_NOT_FOUND);
        }
    }

    public OrderMenu generateOrderMenu(Long menuId) {
        final Menu foundMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BadRequestException(ErrorType.MENU_NOT_FOUND));
        final OrderMenu orderMenu = new OrderMenu(foundMenu.getName(), foundMenu.getPrice());
        return orderMenuRepository.save(orderMenu);
    }
}
