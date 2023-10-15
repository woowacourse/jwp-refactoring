package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);

        MenuProduct 후라이드_1개 = 메뉴상품(후라이드.getId(), 1);
        MenuProduct 양념치킨_1개 = 메뉴상품(양념치킨.getId(), 1);

        Menu menu = 메뉴("후1양1", 25000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when
        Menu created = menuService.create(menu);

        // then
        assertThat(menuDao.findById(created.getId())).isPresent();
    }

    @Test
    void 메뉴의_가격이_0원_미만일_경우_생성할_수_없다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);

        MenuProduct 후라이드_1개 = 메뉴상품(후라이드.getId(), 1);
        MenuProduct 양념치킨_1개 = 메뉴상품(양념치킨.getId(), 1);

        Menu menu = 메뉴("후1양1", -10000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴그룹일_경우_생성할_수_없다() {
        // given
        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);

        MenuProduct 후라이드_1개 = 메뉴상품(후라이드.getId(), 1);
        MenuProduct 양념치킨_1개 = 메뉴상품(양념치킨.getId(), 1);

        Long wrongMenuGroupId = 999L;

        Menu menu = 메뉴("후1양1", 25000, wrongMenuGroupId, List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품일_경우_생성할_수_없다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

        Long wrongProductId1 = 998L;
        Long wrongProductId2 = 999L;

        MenuProduct 후라이드_1개 = 메뉴상품(wrongProductId1, 1);
        MenuProduct 양념치킨_1개 = 메뉴상품(wrongProductId2, 1);

        Menu menu = 메뉴("후1양1", 25000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품가격과_수량의_곱의_합보다_클_경우_생성할_수_없다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);

        MenuProduct 후라이드_1개 = 메뉴상품(후라이드.getId(), 1);
        MenuProduct 양념치킨_1개 = 메뉴상품(양념치킨.getId(), 1);

        Menu menu = 메뉴("후1양1", 35000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_목록을_조회할_수_있다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);

        Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
        MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
        후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

        Menu 양념메뉴 = menuDao.save(메뉴("이만원", 20000, 두마리메뉴.getId()));
        MenuProduct 싼양념치킨상품 = menuProductDao.save(메뉴상품(양념메뉴.getId(), 양념치킨.getId(), 1));
        양념메뉴.setMenuProducts(List.of(싼양념치킨상품));

        // when
        List<Menu> findList = menuService.list();

        // then
        assertThat(findList).usingRecursiveComparison()
                .isEqualTo(List.of(후라이드메뉴, 양념메뉴));
    }
}
