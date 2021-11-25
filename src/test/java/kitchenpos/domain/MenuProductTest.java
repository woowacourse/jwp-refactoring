package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductCalculator;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuProduct 도메인 테스트")
@ExtendWith(MockitoExtension.class)
class MenuProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuProductCalculator menuProductCalculator;

    private static Stream<Arguments> providePriceAndQuantityAndTotalPrice() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(15_000L), 2L, BigDecimal.valueOf(30_000L)),
            Arguments.of(BigDecimal.valueOf(1_000L), 3L, BigDecimal.valueOf(3_000L)),
            Arguments.of(BigDecimal.valueOf(10_000L), 1L, BigDecimal.valueOf(10_000L))
        );
    }

    @DisplayName("[성공] MenuProduct에 포함된 상품들의 값의 합 계산")
    @ParameterizedTest
    @MethodSource({"providePriceAndQuantityAndTotalPrice"})
    void calculatePrice_Success(BigDecimal price, long quantity, BigDecimal expected) {
        // given
        given(productRepository.findById(1L))
            .willReturn(Optional.of(new Product("상품", price)));
        MenuProduct menuProduct = new MenuProduct(1L, quantity);

        // when
        BigDecimal totalPrice = menuProduct.calculatePrice(menuProductCalculator);

        // then
        assertThat(totalPrice).isEqualTo(expected);

    }
}
