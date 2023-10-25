package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuMapper(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public Menu toDomain(MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final Menu menu = menuRepository.save(new Menu(request.getName(), Price.of(request.getPrice()), menuGroup));

        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            final Product product = productRepository.getById(menuProduct.getProductId());
            menuProductRepository.save(
                    new MenuProduct(menu, product.getName(), product.getPrice(), menuProduct.getQuantity())
            );
        }
        return menu;
    }
}
