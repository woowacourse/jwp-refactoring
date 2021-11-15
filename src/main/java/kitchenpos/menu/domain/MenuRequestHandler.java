package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.menu.application.MenuProductService;
import kitchenpos.menu.dto.MenuRequestEvent;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuRequestHandler {

    private final MenuRepository menuRepository;
    private final MenuProductService menuProductService;
    private final ProductService productService;

    public MenuRequestHandler(MenuRepository menuRepository,
        MenuProductService menuProductService,
        ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuProductService = menuProductService;
        this.productService = productService;
    }

    @EventListener
    public void create(MenuRequestEvent event) {
        final Menu savedMenu = menuRepository.save(event.getMenu());

        List<Product> products = productService.findAllById(event.getProductIds());

        List<MenuProduct> menuProducts = menuProductService
            .saveAll(savedMenu, products, event.getQuantity());
        savedMenu.addMenuProducts(menuProducts);
    }
}
