package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.repository.menu.MenuProductRepository;
import kitchenpos.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuProductService {
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public List<MenuProduct> createMenuProduct(Menu menu, List<MenuProductRequest> menuProductRequests) {
        Map<Long, Long> productIdToQuantity = menuProductRequests.stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));
        List<Product> products = productRepository.findAllById(productIdToQuantity.keySet());
        MenuProducts menuProducts = MenuProducts.of(menu, products, productIdToQuantity);
        return menuProductRepository.saveAll(menuProducts.getMenuProducts());
    }

    public List<MenuProduct> findAll() {
        return menuProductRepository.findAll();
    }
}
