package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
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

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private ProductDao productDao;
    private MenuService menuService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }


    @Test
    @DisplayName("메뉴 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_등록_성공_저장() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 이벤트 치킨");
        menu.setPrice(new BigDecimal("8000"));
        menu.setMenuProducts(List.of(menuProduct1));
        menu.setMenuGroupId(4L);
        final Menu saved = menuService.create(menu);

        // then
        assertThat(menuService.list())
                .map(Menu::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("메뉴 등록 시 이름이 있어야 한다.")
    void 메뉴_등록_실패_이름_없음() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal("10000"));
        menu.setMenuProducts(List.of(menuProduct1));
        menu.setMenuGroupId(4L);

        // then
        /// TODO: 2023/10/12 DB 가기 전에 예외처리하기
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴 등록 시 가격이 있어야 한다.")
    void 메뉴_등록_실패_가격_없음() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 치킨");
        menu.setMenuProducts(List.of(menuProduct1));
        menu.setMenuGroupId(4L);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 시 가격은 0 이상 이어야 한다.")
    void 메뉴_등록_실패_가격_음수() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 치킨");
        menu.setPrice(new BigDecimal("-1000"));
        menu.setMenuProducts(List.of(menuProduct1));
        menu.setMenuGroupId(4L);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 1 종류 이상의 메뉴 상품으로 구성되어 있다.")
    void 메뉴_등록_실패_메뉴_상품_없음() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 치킨");
        menu.setPrice(new BigDecimal("10000"));
        menu.setMenuProducts(Collections.emptyList());
        menu.setMenuGroupId(4L);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 1개의 메뉴 그룹에 속한다.")
    void 메뉴_등록_실패_메뉴_그룹_없음() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 치킨");
        menu.setPrice(new BigDecimal("10000"));
        menu.setMenuProducts(List.of(menuProduct1));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 시 가격은 상품 총액보다 클 수 없다.")
    void 메뉴_등록_실패_상품_총액보다_큰_가격() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);

        // when
        final Menu menu = new Menu();
        menu.setName("할인 치킨");
        menu.setPrice(new BigDecimal(Long.MAX_VALUE));
        menu.setMenuProducts(List.of(menuProduct1));
        menu.setMenuGroupId(4L);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
