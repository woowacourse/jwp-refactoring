package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuProductService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(MenuRepository menuRepository, ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Long create(final Long menuId, final Long productId, Long quantity) {
        final Menu menu = menuRepository.getById(menuId);
        final Product product = productRepository.getById(productId);
        return menuProductRepository.save(new MenuProduct(menu, product.getId(), quantity)).getId();
    }

}
