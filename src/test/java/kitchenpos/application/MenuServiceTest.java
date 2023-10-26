package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private static final Product PRODUCT_1000 = ProductFixture.builder()
        .withName("1000상품")
        .withId(1L)
        .withPrice(1000L)
        .build();

    private static final Product PRODUCT_500 = ProductFixture.builder()
        .withName("500상품")
        .withId(2L)
        .withPrice(500L)
        .build();

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Nested
        class 메뉴_가격_정책 {

            @Test
            void 메뉴_생성_시_가격이_음수라면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest(null, BigDecimal.valueOf(-1000L), null,
                    null);

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_생성_시_가격을_보내지_않으면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest("상품", null, null,
                    null);

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_상품_가격의_총합이_메뉴의_가격보다_높으면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest(
                    "상품",
                    BigDecimal.valueOf(6600),
                    1L,
                    List.of(
                        new MenuProductCreateRequest(1L, 5L),
                        new MenuProductCreateRequest(2L, 3L))
                );

                given(menuGroupRepository.findById(anyLong()))
                    .willReturn(Optional.of(new MenuGroup(null, "menuGroup")));

                given(productRepository.findAllByIdIn(anyList()))
                    .willReturn(List.of(
                        PRODUCT_1000,
                        PRODUCT_500
                    ));

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Test
        void 메뉴_그룹이_저장된_메뉴_그룹이_아니면_예외() {
            // given
            MenuCreateRequest request = new MenuCreateRequest(
                "상",
                BigDecimal.valueOf(5400),
                1L,
                List.of(new MenuProductCreateRequest(1L, 5L)));

            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_메뉴_상품에_넣으면_예외() {
            // given
            MenuCreateRequest request = new MenuCreateRequest(
                "상품",
                BigDecimal.valueOf(5400),
                1L,
                List.of(new MenuProductCreateRequest(1L, 5L)));

            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(null, "menuGroup")));
            given(productRepository.findAllByIdIn(anyList()))
                .willReturn(Collections.emptyList());

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_완료() {
            String menuName = "name";
            BigDecimal menuPrice = BigDecimal.valueOf(5400);
            long menuGroupId = 1L;
            MenuProductCreateRequest firstMenuProductRequest = new MenuProductCreateRequest(1L, 5L);
            MenuProductCreateRequest secondMenuProductRequest = new MenuProductCreateRequest(2L, 3L);
            MenuCreateRequest request = new MenuCreateRequest(
                menuName,
                menuPrice,
                menuGroupId,
                List.of(
                    firstMenuProductRequest,
                    secondMenuProductRequest)
            );

            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(null, "menuGroup")));
            given(productRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    PRODUCT_1000,
                    PRODUCT_500
                ));

            Long menuId = 1L;
            Menu menu = new Menu(
                menuId,
                menuName,
                new Price(menuPrice),
                menuGroupId,
                List.of(
                    new MenuProduct(1L, PRODUCT_1000, null, 5),
                    new MenuProduct(2L, PRODUCT_500, null, 3))
            );
            given(menuRepository.save(any()))
                .willReturn(menu);

            // when
            MenuResponse actual = menuService.create(request);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.getMenuGroupId()).isEqualTo(menuGroupId);
                assertThat(actual.getMenuProducts()).hasSize(2);
                assertThat(actual.getMenuProducts())
                    .allMatch(menuProduct -> menuProduct.getMenuId().equals(menuId));
            });
        }

    }

    @Test
    void 메뉴_목록_검색() {
        // given
        long menuId = 1L;
        MenuProduct firstMenuProduct = MenuProductFixture.builder()
            .withMenuId(menuId)
            .build();
        MenuProduct secondMenuProduct = MenuProductFixture.builder()
            .withMenuId(menuId)
            .build();

        List<MenuProduct> ofFirstMenu = List.of(firstMenuProduct, secondMenuProduct);
        Menu menu = MenuFixture.builder()
            .withId(menuId)
            .withName("상품")
            .withPrice(0L)
            .build();

        given(menuRepository.findAll())
            .willReturn(List.of(menu));

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
