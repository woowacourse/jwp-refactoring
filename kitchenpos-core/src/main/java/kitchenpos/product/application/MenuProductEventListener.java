package kitchenpos.product.application;

import kitchenpos.common.dto.request.MenuProductDto;
import kitchenpos.menu.application.event.CreateMenuProductEvent;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.domain.repository.MenuProductRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Component
public class MenuProductEventListener {

    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuProductEventListener(final MenuProductRepository menuProductRepository, final ProductRepository productRepository) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @EventListener
    public void handleMenuProduct(final CreateMenuProductEvent event) {
        final List<MenuProductDto> menuProductDtos = event.getMenuProductDtos();
        final Menu menu = event.getMenu();
        final Products products = findProducts(menuProductDtos);
        createMenuProducts(menuProductDtos, menu, products);
    }

    private Products findProducts(final List<MenuProductDto> productDtos) {
        final List<Product> products = new ArrayList<>();
        for (final MenuProductDto menuProductDto : productDtos) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                                                     .orElseThrow(IllegalArgumentException::new);
            products.add(product);
        }
        return new Products(products);
    }

    private void createMenuProducts(final List<MenuProductDto> menuProductDtos, final Menu savedMenu, final Products products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = products.findProductById(menuProductDto.getProductId());
            final MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductDto.getQuantity());
            menuProducts.add(menuProduct);
        }
        menuProductRepository.saveAll(menuProducts);
    }
}
