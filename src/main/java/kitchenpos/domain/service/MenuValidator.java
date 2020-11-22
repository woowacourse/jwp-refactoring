package kitchenpos.domain.service;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final ProductDao productDao;

    public MenuValidator(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Menu getValidMenu(MenuCreateRequest request) {
        validateMenuPrice(request);

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
            .map(it -> new MenuProduct(null, null, it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
        return new Menu(
            null,
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            menuProducts
        );
    }

    private void validateMenuPrice(MenuCreateRequest request) {
        final Map<Long, BigDecimal> productPrice = getProductPrices(getProductIds(request));
        final BigDecimal sumOfMenuProduct = request.getMenuProducts().stream()
            .reduce(
                BigDecimal.ZERO,
                (sum, menuProduct) -> sum.add(multiply(productPrice, menuProduct)),
                BigDecimal::add
            );
        if (request.getPrice().compareTo(sumOfMenuProduct) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Map<Long, BigDecimal> getProductPrices(List<Long> productIds) {
        return productIds.stream()
            .collect(Collectors.toMap(Function.identity(), this::getProductPrice));
    }

    private BigDecimal getProductPrice(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(IllegalArgumentException::new)
            .getPrice();
    }

    private List<Long> getProductIds(MenuCreateRequest request) {
        return request.getMenuProducts()
            .stream()
            .map(MenuProductCreateRequest::getProductId)
            .collect(Collectors.toList());
    }

    private BigDecimal multiply(Map<Long, BigDecimal> productPrice, MenuProductCreateRequest request) {
        final BigDecimal price = productPrice.get(request.getProductId());
        final BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
        return price.multiply(quantity);
    }
}
