package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Nested
    class 메뉴를_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            Menu menu = MenuFixture.메뉴_엔티티_A;

            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(ProductFixture.상품_엔티티_A));
            given(menuDao.save(any(Menu.class)))
                    .willReturn(menu);

            Menu response = menuService.create(menu);

            assertThat(response).usingRecursiveComparison().isEqualTo(response);
        }

        @Test
        void 상품_가격이_NULL_이면_예외가_발생한다() {
            Menu 메뉴_엔티티_B = MenuFixture.메뉴_엔티티_B_가격_NULL;

            assertThatThrownBy(() -> menuService.create(메뉴_엔티티_B))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격이_0보다_작으면_예외가_발생한다() {
            Menu 메뉴_엔티티_C = MenuFixture.메뉴_엔티티_C_가격_음수;

            assertThatThrownBy(() -> menuService.create(메뉴_엔티티_C))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않는_그룹이면_예외가_발생한다() {
            Menu 메뉴_엔티티_A = MenuFixture.메뉴_엔티티_A;
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(false);

            assertThatThrownBy(() -> menuService.create(메뉴_엔티티_A))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_상품이_존재하지_않는_상품이면_예외가_발생한다() {
            Menu 메뉴_엔티티_A = MenuFixture.메뉴_엔티티_A;

            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> menuService.create(메뉴_엔티티_A))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_각_상품_가격의_합보다_크면_예외가_발생한다() {
            Menu 메뉴_엔티티_D_가격_합계_오류 = MenuFixture.메뉴_엔티티_D_가격_합계_오류;

            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(ProductFixture.상품_엔티티_A));

            assertThatThrownBy(() -> menuService.create(메뉴_엔티티_D_가격_합계_오류))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_메뉴를_조회한다() {
        Menu 메뉴_엔티티_A = MenuFixture.메뉴_엔티티_A;
        MenuProduct 메뉴_상품_엔티티_A = MenuProductFixture.메뉴_상품_엔티티_A;
        given(menuDao.findAll())
                .willReturn(List.of(메뉴_엔티티_A));
        given(menuProductDao.findAllByMenuId(anyLong()))
                .willReturn(List.of(메뉴_상품_엔티티_A));

        List<Menu> menus = menuService.list();

        assertThat(menus).contains(메뉴_엔티티_A);
    }
}
