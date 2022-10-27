package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuCommand menuCommand) {
        Menu menu = menuCommand.toEntity();
        MenuProducts menuProducts = menuCommand.toMenuProducts();
        menuValidator.validate(menu.getMenuGroupId(), menuProducts, menu.getPrice());
        Menu savedMenu = menuRepository.save(menu);
        savedMenu.addMenuProducts(menuProducts);
        menuRepository.flush();
        return MenuResponse.from(menuRepository.findById(savedMenu.getId()).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }
}
