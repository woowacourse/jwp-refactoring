package kitchenpos.application.menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.support.money.Money;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    private final ProductRepository productRepository;

    public MenuMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Menu toMenu(MenuRequest request) {
        Map<Long, Product> products = getProducts(request);
        List<MenuProduct> menuProducts = toMenuProducts(request, products);
        return new Menu(request.getName(), Money.valueOf(request.getPrice()), request.getMenuGroupId(), menuProducts);
    }

    private Map<Long, Product> getProducts(MenuRequest request) {
        return productRepository.findAllByIdIn(request.getMenuProductIds()).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request, Map<Long, Product> products) {
        return request.getMenuProducts().stream()
                .map(menuProduct -> toMenuProduct(menuProduct, products.get(menuProduct.getProductId())))
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProduct, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }
        return new MenuProduct(product.getId(), product.getName(), product.getPrice(), menuProduct.getQuantity());
    }
}
