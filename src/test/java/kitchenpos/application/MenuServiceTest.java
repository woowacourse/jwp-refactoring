package kitchenpos.application;

import kitchenpos.application.fixture.MenuServiceFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends MenuServiceFixture {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @Test
    void 메뉴를_등록한다() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.ofNullable(상품1))
                                             .willReturn(Optional.ofNullable(상품2));
        given(menuDao.save(any(Menu.class))).willReturn(저장된_메뉴);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(메뉴_상품1)
                                                          .willReturn(메뉴_상품2);

        final Menu menu = new Menu(메뉴_이름, 메뉴_가격, 메뉴_그룹_아이디, 메뉴_상품들);

        // when
        final Menu actual = menuService.create(menu);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(저장된_메뉴);
        });
    }

    @ParameterizedTest
    @NullSource
    void 가격이_null이라면_예외를_반환한다(BigDecimal price) {
        // given
        final Menu menu = new Menu(메뉴_이름, price, 메뉴_그룹_아이디, 메뉴_상품들);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_음수라면_예외를_반환한다() {
        // given
        final Menu menu = new Menu(메뉴_이름, 메뉴_가격이_음수, 메뉴_그룹_아이디, 메뉴_상품들);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹이라면_예외를_반환한다() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        final Menu menu = new Menu(메뉴_이름, 메뉴_가격, 존재하지_않는_메뉴_그룹_아이디, 메뉴_상품들);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이라면_예외를_반환한다() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.empty());

        final Menu menu = new Menu(메뉴_이름, 메뉴_가격, 메뉴_그룹_아이디, 존재하지_않는_상품_가진_메뉴_상품들);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품들의_값보디_메뉴의_가격이_더_크다면_예외를_반환한다() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.ofNullable(상품1))
                                             .willReturn(Optional.ofNullable(상품2));

        final Menu menu = new Menu(메뉴_이름, 상품_합보다_큰_메뉴_가격, 메뉴_그룹_아이디, 메뉴_상품들);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        given(menuService.list()).willReturn(저장된_메뉴들);

        // when
        final List<Menu> actual = menuService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(저장된_메뉴1);
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(저장된_메뉴2);
        });
    }
}
