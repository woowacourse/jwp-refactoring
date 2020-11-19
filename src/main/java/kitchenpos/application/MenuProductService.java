package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.domain.Products;

@Service
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuProductService(
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public void validSumIsLowerThanPrice(Money price, MenuProducts menuProducts) {
        List<Long> productIds = menuProducts.extractProductIds();
        Products products = new Products(productRepository.findAllById(productIds));

        Money sum = menuProducts.calculateProductsPriceSum(products);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void associateMenuProductsAndMenu(MenuProducts menuProducts, Menu savedMenu) {
        menuProducts.associateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
        savedMenu.setMenuProducts(menuProducts);
    }
}
