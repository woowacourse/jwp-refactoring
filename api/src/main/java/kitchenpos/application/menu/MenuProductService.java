package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.dto.menu.MenuRequest;
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
    public void addMenuToMenuProduct(MenuRequest menuRequest, Menu menu) {
        menuProductValidator.validate(menuRequest.getMenuProducts(), menu);
        List<MenuProduct> menuProducts = menuRequest.toMenuProducts(menu);
        menuProductRepository.saveAll(menuProducts);
    }
}
