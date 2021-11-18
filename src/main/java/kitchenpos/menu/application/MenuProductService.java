package kitchenpos.menu.application;

import kitchenpos.event.MenuProductValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final MenuProductValidator menuProductValidator;

    public MenuProductService(MenuProductRepository menuProductRepository, MenuProductValidator menuProductValidator) {
        this.menuProductRepository = menuProductRepository;
        this.menuProductValidator = menuProductValidator;
    }

    @Transactional
    public List<MenuProduct> addMenuToMenuProduct(MenuRequest menuRequest, Menu menu) {
        menuProductValidator.validate(menuRequest.getMenuProducts(), menu);
        List<MenuProduct> menuProducts = menuRequest.toMenuProducts(menu);
        return menuProductRepository.saveAll(menuProducts);
    }
}
