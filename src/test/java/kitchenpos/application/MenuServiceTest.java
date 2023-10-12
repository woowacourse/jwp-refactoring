package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Nested
    class 생성 {

        @Test
        void 성공() {
            // given
            Product productA = productDao.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            Product productB = productDao.save(new Product("치즈볼", BigDecimal.valueOf(1000.00)));
            Long menuGroupId = menuGroupDao.save(new MenuGroup("스폐셜")).getId();
            MenuProduct menuProductA = new MenuProduct(productA.getId(), 2);
            MenuProduct menuProductB = new MenuProduct(productB.getId(), 2);
            Menu menu = new Menu("고추바사삭 스폐셜 세트", BigDecimal.valueOf(22000.00), menuGroupId, Arrays.asList(menuProductA, menuProductB));

            // when
            Menu actual = menuService.create(menu);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getMenuProducts())
                    .allSatisfy(it -> assertThat(it.getSeq()).isPositive())
            );
        }

        @Test
        void 가격이_음수면_예외() {
            // given
            Long menuGroupId = menuGroupDao.save(new MenuGroup("순살")).getId();
            Menu menu = new Menu("고추바사삭", BigDecimal.valueOf(-2000L), menuGroupId, Collections.emptyList());

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_아이디의_메뉴그룹이_없으면_예외() {
            // given
            Menu menu = new Menu("고추바사삭", BigDecimal.valueOf(2000L), 1L, Collections.emptyList());

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_상품이_없으면_예외() {
            // given
            Long menuGroupId = menuGroupDao.save(new MenuGroup("순살")).getId();
            MenuProduct menuProduct = new MenuProduct(1L, 2);
            Menu menu = new Menu("고추바사삭", BigDecimal.valueOf(20000.00), menuGroupId, Arrays.asList(menuProduct));

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴상품_가격의_합이_메뉴_가격보다_크면_예외() {
            // given
            Long menuGroupId = menuGroupDao.save(new MenuGroup("순살")).getId();
            Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
            Menu menu = new Menu("고추바사삭", BigDecimal.valueOf(21000.00), menuGroupId, Arrays.asList(menuProduct));

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_메뉴를_반환() {
        // given
        Product productA = productDao.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
        Product productB = productDao.save(new Product("치즈볼", BigDecimal.valueOf(2000.00)));
        Product productC = productDao.save(new Product("감튀", BigDecimal.valueOf(1000.00)));

        MenuProduct menuProductA = new MenuProduct(productA.getId(), 2);
        MenuProduct menuProductB = new MenuProduct(productB.getId(), 2);
        MenuProduct menuProductC = new MenuProduct(productC.getId(), 2);

        Long menuGroupIdA = menuGroupDao.save(new MenuGroup("치즈볼 세트")).getId();
        Long menuGroupIdB = menuGroupDao.save(new MenuGroup("감튀 세트")).getId();
        Menu menuA = new Menu("고추바사삭 스폐셜 세트", BigDecimal.valueOf(24000.00), menuGroupIdA, Arrays.asList(menuProductA, menuProductB));
        Menu menuB = new Menu("고추바사삭 스폐셜 세트", BigDecimal.valueOf(22000.00), menuGroupIdB, Arrays.asList(menuProductA, menuProductC));
        List<Menu> expected = new ArrayList<>();

        expected.add(menuService.create(menuA));
        expected.add(menuService.create(menuB));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
