package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.MenuProductFixture.존재하지_않는_상품을_가진_메뉴_상품;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_성공적으로_저장한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                19000L,
                savedMenuGroup.getId(),
                List.of(toMenuProductCreateRequest(menuProduct))
        );

        // when
        Long savedMenuId = menuService.create(request).getId();

        // then
        Menu actual = menuRepository.findById(savedMenuId).get();
        Menu expected = 메뉴_생성(19000L, savedMenuGroup.getId(), menuProduct);
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(Long.class, BigDecimal.class)
                        .isEqualTo(expected),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice())
        );
    }

    @Test
    void 가격이_null_이라서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                null,
                savedMenuGroup.getId(),
                List.of(toMenuProductCreateRequest(menuProduct))
        );

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_음수여서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                -1L,
                savedMenuGroup.getId(),
                List.of(toMenuProductCreateRequest(menuProduct))
        );

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않아_저장에_실패한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                19000L,
                Long.MAX_VALUE,
                List.of(toMenuProductCreateRequest(menuProduct))
        );

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_상품에_있는_상품이_존재하지_않는_메뉴이면_저장에_실패한다() {
        // given
        MenuProduct invalidMenuProduct = 존재하지_않는_상품을_가진_메뉴_상품();
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                19000L,
                savedMenuGroup.getId(),
                List.of(toMenuProductCreateRequest(invalidMenuProduct))
        );

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_상품의_가격_합이_메뉴의_가격보다_낮으면_저장에_실패한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuCreateRequest request = new MenuCreateRequest(
                "메뉴",
                10000L + 10000L + 1L,
                savedMenuGroup.getId(),
                List.of(toMenuProductCreateRequest(menuProduct))
        );

        // expect
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회한다() {
        // given
        List<Menu> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Product savedProduct = productRepository.save(후추_치킨_10000원());
            MenuProduct menuProduct = 메뉴_상품(savedProduct.getId(), 2);
            MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
            MenuCreateRequest request = new MenuCreateRequest(
                    "메뉴",
                    19000L,
                    savedMenuGroup.getId(),
                    List.of(toMenuProductCreateRequest(menuProduct))
            );
            expected.add(menuService.create(request));
        }

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long.class, BigDecimal.class)
                .isEqualTo(expected);
    }

    private MenuProductCreateRequest toMenuProductCreateRequest(MenuProduct menuProduct) {
        Long productId = Optional.ofNullable(menuProduct.getProductId())
                .orElse(Long.MAX_VALUE);

        return new MenuProductCreateRequest(
                productId,
                menuProduct.getQuantity()
        );
    }

}
