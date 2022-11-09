package kitchenpos.menu.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.resitory.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MenuValidatorTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final MenuGroupRepository menuGroupRepository = Mockito.mock(MenuGroupRepository.class);
    private final MenuValidator menuValidator = new MenuValidator(productRepository, menuGroupRepository);
    private final Product product1 = new Product("상품1", new Price(new BigDecimal(5000)));
    private final Price price = new Price(new BigDecimal(10000));
    MenuProduct menuProduct = new MenuProduct(1L, new Quantity(2L));

    @DisplayName("MenuGroup이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void validate_Exception_MenuGroupNotFound() {
        Long notFoundMenuGroupId = 2L;
        when(menuGroupRepository.existsById(notFoundMenuGroupId))
                .thenReturn(false);
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));

        assertThatThrownBy(() -> menuValidator.validateCreation(price, notFoundMenuGroupId, List.of(menuProduct)))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @DisplayName("Product 수량에 따른 가격 합보다 Menu의 가격이 더 크다면 예외를 발생시킨다.")
    @Test
    void validate_Exception_HigherMenuPrice() {
        Long menuGroupId = 1L;
        when(menuGroupRepository.existsById(any()))
                .thenReturn(true);
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));
        int higherPrice = 10001;

        assertThatThrownBy(() -> menuValidator.validateCreation(
                new Price(new BigDecimal(higherPrice)), menuGroupId, List.of(menuProduct)))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("Product가 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void validate_Exception_NotFoundProduct() {
        Long menuGroupId = 1L;
        when(menuGroupRepository.existsById(any()))
                .thenReturn(true);
        when(productRepository.findById(1L))
                .thenThrow(new ProductNotFoundException());

        assertThatThrownBy(() -> menuValidator.validateCreation(price, menuGroupId, List.of(menuProduct)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @DisplayName("존재하는 Product로 존재하는 MenuProduct에 Product 수량에 따른 가격 합보다 작은 가격으로 Menu를 생성할 수 있다.")
    @Test
    void validate_Pass() {
        Long menuGroupId = 1L;
        when(menuGroupRepository.existsById(any()))
                .thenReturn(true);
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));

        assertThatCode(() -> menuValidator.validateCreation(price, menuGroupId, List.of(menuProduct)))
                .doesNotThrowAnyException();
    }
}
