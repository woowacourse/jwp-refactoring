package kitchenpos.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.DomainTestFixture;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculateProductPriceServiceTest {

    @Test
    @DisplayName("메뉴 상품의 가격 합을 계산한다.")
    void calculateMenuProductPriceSum() {
        final Product product1 = new Product(1L, "상품1", BigDecimal.valueOf(1000L));
        final Product product2 = new Product(2L, "상품2", BigDecimal.valueOf(1500L));
        final Product product3 = new Product(3L, "상품3", BigDecimal.valueOf(5000L));
        final Menu testMenu = DomainTestFixture.getTestMenu();
        final MenuProduct menuProduct1 = new MenuProduct(testMenu, 1L, 1);
        final MenuProduct menuProduct2 = new MenuProduct(testMenu, 2L, 1);
        final MenuProduct menuProduct3 = new MenuProduct(testMenu, 3L, 2);
        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(productRepository.findById(3L)).thenReturn(Optional.of(product3));
        final CalculateProductPriceService calculateProductPriceService
                = new CalculateProductPriceService(productRepository);

        final BigDecimal sum = calculateProductPriceService.calculateMenuProductPriceSum(
                List.of(
                        menuProduct1,
                        menuProduct2,
                        menuProduct3
                )
        );

        assertThat(sum.longValue()).isEqualTo(12500L);
    }
}