package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequestEvent;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ApplicationEventPublisher publisher;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupService menuGroupService,
        final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.publisher = publisher;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        menuGroupService.check(menuRequest.getMenuGroupId());

        Menu menu = new Menu(
            menuRequest.getName(),
            BigDecimal.valueOf(menuRequest.getPrice()),
            menuRequest.getMenuGroupId()
        );

        publisher.publishEvent(new MenuRequestEvent(menu, menuRequest));

        return menu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public void checkCount(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        long count = menuIds.stream()
            .map(menuRepository::countById)
            .reduce(0L, Long::sum);

        if (orderLineItems.size() != count) {
            throw new IllegalArgumentException("orderLineItems의 크기는 메뉴의 수와 같아야 합니다. ");
        }
    }
}
