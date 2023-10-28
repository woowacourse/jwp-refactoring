package kitchenpos.menu_group.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu_group.domain.MenuProduct;
import kitchenpos.menu_group.domain.repository.MenuProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuProductService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(MenuRepository menuRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Long create(final Long menuId, final Long productId, Long quantity) {
        final Menu menu = menuRepository.getById(menuId);
        return menuProductRepository.save(new MenuProduct(menu, productId, quantity)).getId();
    }

}
