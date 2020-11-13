package kitchenpos.application.event.listener;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.event.ValidateMenuPriceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MenuEventListener {
    private final ProductDao productDao;

    public MenuEventListener(ProductDao productDao) {
        this.productDao = productDao;
    }

    @EventListener
    public void validateMenuPrice(ValidateMenuPriceEvent event) {
        final Menu menu = (Menu) event.getSource();
        final Map<Long, BigDecimal> productPrice = getProductPrices(menu.getProductIds());

        if (menu.isExpensiveThanSumOf(productPrice)) {
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
}
