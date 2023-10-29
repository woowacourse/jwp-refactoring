package kitchenpos.menu.domain.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuGroup;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.menu.supports.MenuGroupFixture;
import kitchenpos.menu.supports.MenuProductFixture;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.supports.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuValidator menuValidator;


    @Test
    void 메뉴_그룹은_DB에_존재해야한다() {
        // given
        Long menuGroupId = 1L;
        MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
        Menu menu = MenuFixture.fixture().menuGroup(menuGroup).build();

        given(menuGroupRepository.existsById(menuGroupId))
            .willReturn(false);

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }

    @Test
    void 메뉴_상품의_상품은_모두_DB에_존재해야한다() {
        // given
        Long menuGroupId = 1L;
        MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
        List<MenuProduct> menuProducts = List.of(
            MenuProductFixture.fixture().productId(1L).build(),
            MenuProductFixture.fixture().productId(2L).build()
        );
        Menu menu = MenuFixture.fixture().menuGroup(menuGroup).menuProducts(menuProducts).build();

        given(menuGroupRepository.existsById(menuGroupId))
            .willReturn(true);
        given(productRepository.findAllById(eq(List.of(1L, 2L))))
            .willReturn(List.of(ProductFixture.fixture().id(1L).build()));

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    void 메뉴_가격이_상품의_가격_총합보다_크면_예외() {
        // given
        int menuPrice = 9000;
        Long menuGroupId = 1L;
        MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
        List<MenuProduct> menuProducts = List.of(
            MenuProductFixture.fixture().productId(1L).quantity(1L).build(),
            MenuProductFixture.fixture().productId(2L).quantity(2L).build()
        );
        List<Product> products = List.of(
            ProductFixture.fixture().id(1L).price(2000).build(),
            ProductFixture.fixture().id(2L).price(3000).build()
        );
        Menu menu = MenuFixture.fixture().price(menuPrice).menuGroup(menuGroup).menuProducts(menuProducts).build();

        given(menuGroupRepository.existsById(menuGroupId))
            .willReturn(true);
        given(productRepository.findAllById(eq(List.of(1L, 2L))))
            .willReturn(products);

        // when
        assertThatThrownBy(() -> menuValidator.validate(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 가격은 상품의 가격 총 합보다 클 수 없습니다.");
    }

    @Test
    void 검증_성공() {
        // given
        int menuPrice = 8000;
        Long menuGroupId = 1L;
        MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
        List<MenuProduct> menuProducts = List.of(
            MenuProductFixture.fixture().productId(1L).quantity(1L).build(),
            MenuProductFixture.fixture().productId(2L).quantity(2L).build()
        );
        List<Product> products = List.of(
            ProductFixture.fixture().id(1L).price(2000).build(),
            ProductFixture.fixture().id(2L).price(3000).build()
        );
        Menu menu = MenuFixture.fixture().price(menuPrice).menuGroup(menuGroup).menuProducts(menuProducts).build();

        given(menuGroupRepository.existsById(menuGroupId))
            .willReturn(true);
        given(productRepository.findAllById(eq(List.of(1L, 2L))))
            .willReturn(products);

        // when
        assertThatNoException()
            .isThrownBy(() -> menuValidator.validate(menu));
    }
}
