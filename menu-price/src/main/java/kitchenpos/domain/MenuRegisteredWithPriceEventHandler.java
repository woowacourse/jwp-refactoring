package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRegisteredEvent;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class MenuRegisteredWithPriceEventHandler {

    private final ProductRepository productRepository;

    public MenuRegisteredWithPriceEventHandler(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Async
    @EventListener
    public void handle(final MenuRegisteredEvent event) {
        validate(event);
    }

    public void validate(final MenuRegisteredEvent event) {
        Menu menu = event.getMenu();
        validate(menu, getQuantityPerProduct(menu.getMenuProducts()));
    }

    private void validate(final Menu menu, final Map<Product, Long> quantityPerProduct) {
        BigDecimal price = menu.getPrice().getValue();
        BigDecimal totalMenuProductsPrice = getTotalMenuProductsPrice(quantityPerProduct);
        if (price.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException(String.format(
                "메뉴 가격은 상품 가격의 합보다 클 수 없습니다.(메뉴 가격: %d, 상품 가격: %d)",
                price.intValue(),
                totalMenuProductsPrice.intValue()
            ));
        }
    }

    private BigDecimal getTotalMenuProductsPrice(final Map<Product, Long> quantityPerProduct) {
        return quantityPerProduct.keySet().stream()
            .map(product -> product.getPrice().getValue()
                .multiply(BigDecimal.valueOf(quantityPerProduct.get(product))))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Product, Long> getQuantityPerProduct(final MenuProducts menuProducts) {
        Map<Product, Long> products = new HashMap<>();
        for (MenuProduct menuProduct : menuProducts) {
            Long productId = menuProduct.getProductId();
            Product product = productRepository.findById(productId).orElseThrow(() ->
                new IllegalArgumentException(
                    String.format("존재하지 않는 제품이 있습니다.(id: %d)", productId)
                )
            );
            products.put(product, menuProduct.getQuantity().getValue());
        }
        return products;
    }
}
