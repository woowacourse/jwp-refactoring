package kitchenpos.product.application;

import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.common.event.ValidateExistProductEvent;
import kitchenpos.common.event.ValidateSamePriceWithMenuEvent;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductEventHandler {

    private final ProductRepository productRepository;

    public ProductEventHandler(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener
    @Transactional
    public void validateExistProduct(final ValidateExistProductEvent dto) {
        productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }

    @EventListener
    @Transactional
    public void validateSamePriceWithMenu(final ValidateSamePriceWithMenuEvent validateSamePriceWithMenuEvent) {
        final Price menuPrice = Price.from(validateSamePriceWithMenuEvent.getMenuPrice());
        final Price sum = validateSamePriceWithMenuEvent.getProductQuantityDtos().stream()
                .map(dto -> findProductPrice(dto.getProductId()).multiply(dto.getQuantity()))
                .reduce(Price.createZero(), Price::plus);
        if (menuPrice.isGreaterThan(sum)) {
            throw new IllegalArgumentException("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
        }
    }

    private Price findProductPrice(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
        return product.getPrice();
    }
}
