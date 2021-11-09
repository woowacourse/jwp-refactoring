package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final Menu menu) {
        menuValidator.validateCreation(menu);
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional
    public void updateMenuInfo(final Long menuId, final String name, final BigDecimal price) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
        menu.updateInfo(name, price);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
