package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.name.Name;
import kitchenpos.price.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("존재하지 않는 제품일 경우 예외 처리")
    @Test
    void notFoundProduct() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(
            new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                1L,
                new MenuProducts(Collections.singletonList(
                    new MenuProduct(1L, 2L)
                )),
                menuValidator
            )
        ));
    }

    @DisplayName("메뉴 가격이 상품 가격의 합보다 클 경우 예외 처리")
    @Test
    void priceOfMenuIsGreaterThanTotalPriceOfMenuProduct() {
        when(productRepository.findById(any())).thenReturn(
            Optional.of(new Product("후라이드", BigDecimal.valueOf(7500)))
        );
        when(menuGroupRepository.existsById(any())).thenReturn(true);

        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(
            new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                1L,
                new MenuProducts(Collections.singletonList(
                    new MenuProduct(1L, 2L)
                )),
                menuValidator
            )
        ));
    }

    @DisplayName("메뉴 가격과 상품 가격의 합이 같을 경우 예외 처리 하지 않음")
    @Test
    void priceOfMenuEqualsTotalPriceOfMenuProduct() {
        when(productRepository.findById(any())).thenReturn(
            Optional.of(new Product("후라이드", BigDecimal.valueOf(8000)))
        );
        when(menuGroupRepository.existsById(any())).thenReturn(true);

        assertThatCode(() -> menuValidator.validate(
            new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                1L,
                new MenuProducts(Collections.singletonList(
                    new MenuProduct(1L, 2L)
                )),
                menuValidator
            )
        )).doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 메뉴 그룹의 경우 예외 처리")
    @Test
    void notFoundMenuGroup() {
        when(productRepository.findById(any())).thenReturn(
            Optional.of(new Product("후라이드", BigDecimal.valueOf(8000)))
        );
        when(menuGroupRepository.existsById(any())).thenReturn(false);

        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(
            new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                1L,
                new MenuProducts(Collections.singletonList(
                    new MenuProduct(1L, 2L)
                )),
                menuValidator
            )
        ));
    }
}
