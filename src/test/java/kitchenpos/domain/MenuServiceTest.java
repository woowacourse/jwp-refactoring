package kitchenpos.domain;

import static kitchenpos.fixture.MenuFixture.REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록한다() {
            // given
            Menu menu = REQUEST.메뉴_등록_요청();
            given(menuDao.save(any(Menu.class)))
                    .willReturn(menu);
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(ProductFixture.PRODUCT.후라이드_치킨()));

            // when
            Menu result = menuService.create(menu);

            // then
            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(menu);
        }

        @ParameterizedTest(name = "메뉴의 가격이 {0}이면 예외")
        @ValueSource(ints = {-1, -100})
        void 메뉴의_가격이_잘못되면_예외(Integer price) {
            // given
            Menu menu = REQUEST.메뉴_등록_요청();
            menu.setPrice(BigDecimal.valueOf(price));

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_Null이면_예외() {
            Menu menu = REQUEST.메뉴_등록_요청();
            menu.setPrice(null);

            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외() {
            // given
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(false);
            Menu menu = REQUEST.메뉴_등록_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_예외() {
            // given
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.empty());
            Menu menu = REQUEST.메뉴_등록_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "메뉴가 {0}원이고 상품이 {1}원이면 예외")
        @CsvSource(value = {"17000,16999", "17000,10000", "17000,1000", "17000,1", "17000,0", "17000,-1", "17000,-100"})
        void 메뉴_가격이_상품들의_가격_합보다_크면_예외(Long menuPrice, Long productPrice) {
            // given
            Menu menu = REQUEST.메뉴_등록_요청();
            Product product = ProductFixture.PRODUCT.후라이드_치킨();
            menu.setPrice(BigDecimal.valueOf(menuPrice));
            product.setPrice(BigDecimal.valueOf(productPrice));

            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(product));

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            Menu menu = REQUEST.메뉴_등록_요청();
            given(menuDao.findAll())
                    .willReturn(List.of(menu));
            given(menuProductDao.findAllByMenuId(anyLong()))
                    .willReturn(List.of(MenuFixture.MENU_PRODUCT.후라이드_치킨()));

            // when
            List<Menu> result = menuService.list();

            // then
            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(menu));
        }
    }
}
