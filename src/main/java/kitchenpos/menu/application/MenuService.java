package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuUpdateEvent;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.response.MenuResponse;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.domain.Menu;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator,
                       final ApplicationEventPublisher applicationEventPublisher) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public MenuResponse create(final Menu menu) {
        menuValidator.validateCreation(menu);
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse updateMenuDetails(final Long menuId, final String name, final BigDecimal price) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
        applicationEventPublisher.publishEvent(new MenuUpdateEvent(
                new Menu(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts()), menuId));
        menu.updateDetails(name, price);
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
