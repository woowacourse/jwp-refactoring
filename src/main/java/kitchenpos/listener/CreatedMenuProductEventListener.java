package kitchenpos.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.InvalidNumberException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.CreatedMenuProductsEvent;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreatedMenuProductEventListener {

    private final ProductRepository productRepository;

    public CreatedMenuProductEventListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener
    public void listenCreatedMenuProductEvent(final CreatedMenuProductsEvent event) {
        final List<Long> productIds = event.getMenuProductDtos().stream()
                .map(MenuProductDto::getProductId)
                .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllById(productIds);

        BigDecimal sum = BigDecimal.ZERO;

        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final MenuProductDto dto : event.getMenuProductDtos()) {
            final Product product = findByIdIn(products, dto.getProductId());
            menuProducts.add(MenuProduct.of(dto, product.getId()));
            sum = sum.add(product.getPrice().multiply(dto.getQuantity()));
        }

        if (sum.compareTo(event.getPrice()) < 0) {
            throw new InvalidNumberException("상품 가격의 총합보다 메뉴가 더 비쌀 수 없습니다.");
        }

        event.setMenuProducts(menuProducts);
    }

    private Product findByIdIn(final List<Product> products, final Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 상품이 없습니다."));
    }
}
