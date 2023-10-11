package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
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

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Nested
        class 메뉴_가격_정책 {

            @Test
            void 메뉴_생성_시_가격이_음수라면_예외() {
                // given
                Menu menu = MenuFixture.builder()
                    .withPrice(-1000L)
                    .build();

                // when && then
                assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_생성_시_가격을_보내지_않으면_예외() {
                // given
                Menu menu = MenuFixture.builder()
                    .build();

                // when && then
                assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_상품_가격의_총합이_메뉴의_가격보다_높으면_예외() {
                // given
                long firstProductId = 1L;
                int firstProductQuantity = 5;
                long firstProductPrice = 1000L;
                Product firstProduct = ProductFixture.builder()
                    .withPrice(firstProductPrice)
                    .build();

                long secondProductId = 2L;
                int secondProductQuantity = 10;
                long secondProductPrice = 2000L;
                Product secondProduct = ProductFixture.builder()
                    .withPrice(secondProductPrice)
                    .build();

                long firstProductMenuPrice = firstProductPrice * firstProductQuantity;
                long secondProductMenuPrice = secondProductPrice * secondProductQuantity;
                int plusPrice = 100;
                long lowerThanMenuPrice = firstProductMenuPrice + secondProductMenuPrice + plusPrice;

                Menu menu = MenuFixture.builder()
                    .withPrice(lowerThanMenuPrice)
                    .withMenuGroupId(1L)
                    .withMenuProducts(
                        List.of(
                            MenuProductFixture.builder()
                                .withProductId(firstProductId)
                                .withQuantity(firstProductQuantity)
                                .build(),
                            MenuProductFixture.builder()
                                .withProductId(secondProductId)
                                .withQuantity(secondProductQuantity)
                                .build())
                    )
                    .build();

                given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
                given(productDao.findById(anyLong()))
                    .willReturn(
                        Optional.of(firstProduct),
                        Optional.of(secondProduct)
                    );

                // when && then
                assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Test
        void 메뉴_그룹이_저장된_메뉴_그룹이_아니면_예외() {
            // given
            long nonExistentMenuGroupId = 1L;
            Menu menu = MenuFixture.builder()
                .withPrice(1000L)
                .withMenuGroupId(nonExistentMenuGroupId)
                .build();

            given(menuGroupDao.existsById(nonExistentMenuGroupId))
                .willReturn(false);

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_메뉴_상품에_넣으면_예외() {
            // given
            Menu menu = MenuFixture.builder()
                .withPrice(1000L)
                .withMenuGroupId(1L)
                .withMenuProducts(
                    List.of(
                        MenuProductFixture.builder()
                            .withProductId(1L)
                            .build())
                )
                .build();

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_완료() {
            // given
            long firstProductId = 1L;
            int firstProductQuantity = 5;
            long firstProductPrice = 1000L;
            Product firstProduct = ProductFixture.builder()
                .withPrice(firstProductPrice)
                .build();

            long secondProductId = 2L;
            int secondProductQuantity = 10;
            long secondProductPrice = 2000L;
            Product secondProduct = ProductFixture.builder()
                .withPrice(secondProductPrice)
                .build();

            long firstProductMenuPrice = firstProductPrice * firstProductQuantity;
            long secondProductMenuPrice = secondProductPrice * secondProductQuantity;
            long menuPrice = firstProductMenuPrice + secondProductMenuPrice;

            MenuProduct firstMenuProduct = MenuProductFixture.builder()
                .withProductId(firstProductId)
                .withQuantity(firstProductQuantity)
                .build();
            MenuProduct secondMenuProduct = MenuProductFixture.builder()
                .withProductId(secondProductId)
                .withQuantity(secondProductQuantity)
                .build();

            long menuId = 1L;
            long menuGroupId = 1L;
            Menu menu = MenuFixture.builder()
                .withId(menuId)
                .withPrice(menuPrice)
                .withMenuGroupId(menuGroupId)
                .withMenuProducts(
                    List.of(
                        firstMenuProduct,
                        secondMenuProduct)
                )
                .build();

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(anyLong()))
                .willReturn(
                    Optional.of(firstProduct),
                    Optional.of(secondProduct)
                );
            given(menuDao.save(any()))
                .willReturn(menu);
            given(menuProductDao.save(any()))
                .willReturn(firstMenuProduct, secondMenuProduct);

            // when
            Menu actual = menuService.create(menu);

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
        MenuProduct firstMenuProduct = MenuProductFixture.builder().build();
        MenuProduct secondMenuProduct = MenuProductFixture.builder().build();

        List<MenuProduct> ofFirstMenu = List.of(firstMenuProduct, secondMenuProduct);
        Menu menu = MenuFixture.builder()
            .withId(1L)
            .withMenuProducts(ofFirstMenu)
            .build();

        given(menuDao.findAll())
            .willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong()))
            .willReturn(ofFirstMenu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).isEqualTo(List.of(menu));
    }
}
