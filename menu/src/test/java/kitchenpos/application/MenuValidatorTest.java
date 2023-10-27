package kitchenpos.application;

import kitchenpos.MenuValidator;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    void 생성하려는_메뉴의_메뉴그룹이_존재하지_않으면_예외발생() {
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Menu menu = new Menu("menu", BigDecimal.valueOf(10), 10000L);

        assertThatThrownBy(() -> menuValidator.validate(menu, anyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_메뉴의_가격이_음수면_예외발생() {
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Menu menu = new Menu("menu", BigDecimal.valueOf(-1), 10000L);

        assertThatThrownBy(() -> menuValidator.validate(menu, anyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_메뉴의_가격이_존재하지_않으면_예외발생() {
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Menu menu = new Menu("menu", null, 10000L);

        assertThatThrownBy(() -> menuValidator.validate(menu, anyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_메뉴상품이_존재하지_않으면_예외발생() {
        // given
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "good")));

        Menu menu = new Menu("menu", BigDecimal.TEN, 10000L);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MenuProduct menuProduct = new MenuProduct(1L, 3);

        // then
        assertThatThrownBy(() -> menuValidator.validate(menu, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_메뉴의_가격이_메뉴_상품_합보다_크면_예외발생() {
        // given
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "good")));

        BigDecimal productPrice = BigDecimal.TEN;
        Product product = new Product(1L, "chicken", productPrice);
        int menuProductQuantity = 3;
        MenuProduct menuProduct = new MenuProduct(1L, 3);
        BigDecimal maxMenuPrice = productPrice.multiply(BigDecimal.valueOf(menuProductQuantity));

        // when
        Menu menu = new Menu("menu", maxMenuPrice.add(BigDecimal.valueOf(1)), 1L);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // then
        assertThatThrownBy(() -> menuValidator.validate(menu, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
