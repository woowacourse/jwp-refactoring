package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
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
        List<MenuProduct> menuProducts = products.stream()
                .map(product -> new MenuProduct(menu, product, productIdToQuantity.get(product.getId())))
                .collect(Collectors.toList());
        validateTotalAmount(menu, menuProducts);
        return menuProductRepository.saveAll(menuProducts);
    }

    private void validateTotalAmount(Menu menu, List<MenuProduct> menuProducts) {
        Price result = Price.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            result = result.sum(menuProduct.calculateAmount());
        }
        if (menu.isSmallerPrice(result)) {
            throw new IllegalArgumentException("MenuProduct 전부를 합한 금액이 Menu 금액보다 작을 수 없습니다.");
        }
    }

    public List<MenuProduct> findAll() {
        return menuProductRepository.findAll();
    }
}
