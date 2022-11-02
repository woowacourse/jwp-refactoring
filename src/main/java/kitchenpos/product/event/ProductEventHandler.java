package kitchenpos.product.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.request.CreateMenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Component
public class ProductEventHandler {

    private final ProductRepository productRepository;

    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener
    public void verifiedMenuProducts(VerifiedMenuProductsEvent event) {
        BigDecimal totalPrice = calculateTotalPrice(getProducts(event.getMenuProducts()), event.getMenuProducts());

        if (event.getPrice().compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("각 상품 가격의 합보다 큰 가격을 적용할 수 없습니다.");
        }
    }

    private Map<Long, Product> getProducts(List<CreateMenuProductRequest> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(it -> it.getProductId())
            .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllByIdIn(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        return products.stream()
            .collect(Collectors.toMap(
                it -> it.getId(),
                it -> it
            ));
    }

    private BigDecimal calculateTotalPrice(Map<Long, Product> products, List<CreateMenuProductRequest> menuProducts) {
        return menuProducts.stream()
            .map(it -> products.get(it.getProductId()).getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
