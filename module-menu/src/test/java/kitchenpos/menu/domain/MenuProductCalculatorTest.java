package kitchenpos.menu.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductCalculator;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MethodProductCalculator 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuProductCalculatorTest {

    @InjectMocks
    private MenuProductCalculator menuProductCalculator;

    @Mock
    private ProductRepository productRepository;

    private static Stream<Arguments> providePriceAndQuantityAndTotalPrice() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(15_000L), 2L, BigDecimal.valueOf(30_000L)),
            Arguments.of(BigDecimal.valueOf(1_000L), 3L, BigDecimal.valueOf(3_000L)),
            Arguments.of(BigDecimal.valueOf(10_000L), 1L, BigDecimal.valueOf(10_000L))
        );
    }

    @DisplayName("상품의 합계를 계산한다.")
    @ParameterizedTest
    @MethodSource({"providePriceAndQuantityAndTotalPrice"})
    void totalPrice_Success(BigDecimal price, Long quantity, BigDecimal expected) {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, quantity);
        BDDMockito.given(productRepository.findById(ArgumentMatchers.any()))
            .willReturn(Optional.of(new Product("상품", price)));

        // when
        BigDecimal actual = menuProductCalculator.totalPrice(menuProduct);

        // then
        AssertionsForClassTypes.assertThat(actual).isEqualTo(expected);
    }
}
