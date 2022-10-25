package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    @Transactional
    public MenuResponse create(final MenuCommand menuCommand) {
        Menu menu = menuCommand.toEntity();
        MenuProducts menuProducts = menuCommand.toMenuProducts();
        menuValidator.validate(menu.getMenuGroupId(), menuProducts, menu.getPrice());
        Menu savedMenu = menuRepository.save(menu);
        savedMenu.addMenuProducts(menuProducts);
        return MenuResponse.from(savedMenu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
