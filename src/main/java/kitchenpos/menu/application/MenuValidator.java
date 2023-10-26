package kitchenpos.menu.application;

import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductDto;
import kitchenpos.menu.exception.MenuPriceTooExpensiveException;
import kitchenpos.menu.exception.PriceNullOrLessThanOrEqualToZeroException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public void validateMenuCreationPrice(MenuRequest request) {
        validatePriceNotNullAndPositive(request);
        validatePriceByQuantityBiggerThanRequestedPrice(request);
    }

    private void validatePriceNotNullAndPositive(MenuRequest request) {
        BigDecimal requestPrice = request.getPrice();
        if (Objects.isNull(requestPrice) || requestPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceNullOrLessThanOrEqualToZeroException();
        }
    }

    private void validatePriceByQuantityBiggerThanRequestedPrice(MenuRequest request) {
        BigDecimal requestPrice = request.getPrice();
        List<MenuProductDto> menuProductDtos = request.getMenuProductDtos();
        BigDecimal priceByQuantity = calcPriceByQuantity(menuProductDtos);
        if (requestPrice.compareTo(priceByQuantity) > 0) {
            throw new MenuPriceTooExpensiveException();
        }
    }

    private BigDecimal calcPriceByQuantity(List<MenuProductDto> menuProductDtos) {
        List<Long> productIds = menuProductDtos.stream()
                .map(MenuProductDto::getProductId)
                .collect(Collectors.toList());
        Map<Long, Product> productById = productRepository.findAllByIdIn(productIds).stream()
                .collect(toMap(Product::getId, Function.identity()));
        validateRequestedProductExists(productIds, productById);
        return accumulatePriceByQuantity(menuProductDtos, productById);
    }

    private void validateRequestedProductExists(List<Long> productIds,
            Map<Long, Product> productById) {
        if (productIds.size() != productById.size()) {
            throw new ProductNotFoundException();
        }
    }

    private BigDecimal accumulatePriceByQuantity(List<MenuProductDto> menuProductDtos,
            Map<Long, Product> productById) {
        return menuProductDtos.stream()
                .map(dto -> productById.get(dto.getProductId()).getPrice()
                        .multiply(new BigDecimal(dto.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow(RuntimeException::new);
    }
}
