package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
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
