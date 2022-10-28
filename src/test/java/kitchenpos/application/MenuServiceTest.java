package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.getMenu;
import static kitchenpos.fixture.MenuFixture.getMenuProduct;
import static kitchenpos.fixture.MenuFixture.getMenuRequest;
import static kitchenpos.fixture.ProductFixture.getProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest{

    @Autowired
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        final Product product = getProduct();

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(menuGroupDao.existsById(2L)).willReturn(false);
        given(menuDao.save(any())).willReturn(getMenu(1L));
        given(productDao.findById(1L)).willReturn(Optional.of(product));
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴안의 상품들의 가격의 합을 넘을 경우 메뉴를 생성하면 예외가 발생한다.")
    void createWithOverSumProductsPrice() {
        // given
        given(productDao.findById(1L)).willReturn(Optional.of(getProduct(1L, 10000)));
        given(productDao.findById(2L)).willReturn(Optional.of(getProduct(2L, 10000)));

        // when
        final List<MenuProduct> menuProducts = Arrays.asList(getMenuProduct(1L), getMenuProduct(2L));
        final Menu menuRequest = getMenuRequest(21000, menuProducts);

        // then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "가격이 음수일 경우 메뉴를 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-1000, -1})
    void createWithNegativePriceMenu(int price) {
        final Menu menuRequest = getMenuRequest(price);
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
