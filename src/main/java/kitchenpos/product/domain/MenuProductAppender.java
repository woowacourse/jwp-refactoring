package kitchenpos.product.domain;

import java.util.List;
import java.util.Map;
import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuProductAppender {

    private final ProductRepository productRepository;

    public MenuProductAppender(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void append(final Menu menu, final Map<Long, Long> productQuantityMap) {
        final List<Product> products = productRepository.findAllById(productQuantityMap.keySet());
        menu.addProducts(products, productQuantityMap);
    }
}
