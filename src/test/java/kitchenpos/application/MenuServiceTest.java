package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuServiceTest {

    private MenuService sut;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        sut = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L);
        final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 2L, List.of(menuProduct));

        // when
        final Menu createdMenu = sut.create(menu);

        // then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getId()).isNotNull();
        final Menu foundMenu = menuDao.findById(createdMenu.getId()).get();
        assertThat(foundMenu)
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts")
                .isEqualTo(createdMenu);
    }

    @DisplayName("메뉴의 가격은 음수일 수 없다.")
    @Test
    void createWithMinusPrice() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L);
        final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(-1), 2L, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 반드시 함께 등록되어야 한다.")
    @Test
    void createWithNullPrice() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L);
        final Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setMenuGroupId(2L);
        menu.setMenuProducts(List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 반드시 어느 메뉴 그룹에 속해있어야 한다.")
    @Test
    void createWithNonGroup() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L);
        final Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuProducts(List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품(product)의 금액 총합(가격 * 수량) 보다 크면 안된다.")
    @Test
    void createWithLessPriceThenTotalProductPrice() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L);
        final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(16001), 2L, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품(Product)의 조회결과가 없는 경우 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithEmptyProduct() {
        // given
        final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 2L, List.of());

        // when & then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 리스트를 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<Menu> menus = sut.list();

        // then
        assertThat(menus)
                .hasSize(6)
                .extracting(Menu::getName, menu -> menu.getPrice().longValue(), Menu::getMenuGroupId)
                .containsExactlyInAnyOrder(
                        tuple("후라이드치킨", 16000L, 2L),
                        tuple("양념치킨", 16000L, 2L),
                        tuple("반반치킨", 16_000L, 2L),
                        tuple("통구이", 16_000L, 2L),
                        tuple("간장치킨", 17_000L, 2L),
                        tuple("순살치킨", 17_000L, 2L)
                );
    }
}
