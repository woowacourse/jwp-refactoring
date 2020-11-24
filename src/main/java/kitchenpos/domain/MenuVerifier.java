package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class MenuVerifier {
    private final ProductDao productDao;

    public MenuVerifier(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void verifyPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum
                .add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (Objects.nonNull(price) && price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 금액의 합보다 작거나 같아야 합니다.");
        }
    }
}
