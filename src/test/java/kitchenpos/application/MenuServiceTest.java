package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest implements ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴가_메뉴_그룹에_속하지_않으면_예외가_발생한다() {
        // given
        final Menu menu = 메뉴(
                null,
                "두마리치킨",
                menuProductDao.findAll(),
                BigDecimal.valueOf(0)
        );

        // expected
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -100})
    void 메뉴_생성_시_메뉴의_가격은_0원_이상_이어야_한다(final long price) {
        // given
        final Menu menu = 메뉴(
                menuGroupDao.findAll().get(0).getId(),
                "두마리치킨",
                menuProductDao.findAll(),
                BigDecimal.valueOf(price)
        );

        // expected
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_시_메뉴의_가격이_없다면_예외가_발생한다() {
        // given
        final Menu menu = 메뉴(
                menuGroupDao.findAll().get(0).getId(),
                "두마리치킨",
                menuProductDao.findAll(),
                null
        );

        // expected
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_시_가격과_실제_메뉴_상품들의_가격의_총합이_다르면_예외가_발생한다() {
        // given
        final Menu menu = 메뉴(
                menuGroupDao.findAll().get(0).getId(),
                "두마리치킨",
                menuProductDao.findAll(),
                BigDecimal.valueOf(Long.MAX_VALUE)
        );

        // expected
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("치킨"));
        final Product product = productDao.save(상품("100%뼈치킨", BigDecimal.valueOf(18_000)));
        final Menu menu = menuDao.save(메뉴(menuGroup.getId(), "100%뼈치킨메뉴", BigDecimal.valueOf(18_000)));
        final MenuProduct menuProduct = menuProductDao.save(메뉴_상품(menu.getId(), product.getId(), 1L));
        menu.setMenuProducts(List.of(menuProduct));

        // when
        final List<Menu> list = menuService.list();

        //then
        assertThat(list).usingRecursiveFieldByFieldElementComparator().contains(menu);
    }
}
