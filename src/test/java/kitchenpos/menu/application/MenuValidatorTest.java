package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.application.ProductValidationService;
import kitchenpos.product.domain.Price;
import kitchenpos.support.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private ProductValidationService productValidationService;

    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴에 해당하는 제품이 존재하지 않으면 예외가 발생한다.")
    void validate_notExistsProduct() {
        // given
        given(productValidationService.existsProductsByIdIn(Arrays.asList(1L, 2L)))
                .willReturn(false);
        final Menu menu = MenuFixture.createWithPrice(1L, 1000L, 1L, 2L);

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isExactlyInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴에 해당하는 제품들의 수량 가격 합보다 제품 가격이 크면 예외가 발생한다.")
    void validate_expensiveMenuPrice() {
        // given
        final List<Long> productIds = Arrays.asList(1L, 2L);
        given(productValidationService.existsProductsByIdIn(productIds))
                .willReturn(true);
        given(productValidationService.calculateAmountSum(productIds))
                .willReturn(Optional.of(new Price(1000L)));
        final Menu menu = MenuFixture.createWithPrice(1L, 1001L, 1L, 2L);

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }
}
