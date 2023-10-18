package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuDao menuDao;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private MenuProductDao menuProductDao;

    MenuGroup menuGroup;
    MenuProduct menuProduct;
    Menu menu;
    Product product;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(2L, "한마리메뉴");

        menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);

        menu = new Menu(1L, "menu", BigDecimal.valueOf(2000), menuGroup.getId(), List.of(menuProduct));

        product = new Product(1L, "후라이드", BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(product));
        given(menuDao.save(any()))
                .willReturn(menu);
        given(menuProductDao.save(any()))
                .willReturn(menuProduct);
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        MenuDto menuDto = MenuDto.from(menu);

        // when
        MenuDto result = menuService.create(menuDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(menu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        });
    }

    @Test
    void 가격이_0이상인_상품의_메뉴를_생성한다() {
        // given
        MenuDto menuDto = new MenuDto(menu.getId(), menu.getName(), BigDecimal.ZERO, menu.getMenuGroupId(), menu.getMenuProducts());
        product = new Product(product.getId(), product.getName(), BigDecimal.ZERO);

        // when
        MenuDto result = menuService.create(menuDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(menu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        });
    }

    @Test
    void 가격이_0보다_작은_메뉴를_생성하면_예외를_던진다() {
        // given
        MenuDto menuDto = new MenuDto(menu.getId(), menu.getName(), BigDecimal.valueOf(-1), menu.getMenuGroupId(), menu.getMenuProducts());

        // when
        assertThatThrownBy(() -> menuService.create(menuDto))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 메뉴_상품_가격의_합이_메뉴_가격보다_크면_예외를_던진다() {
        // given
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setQuantity(1L);
            product = new Product(product.getId(), product.getName(), BigDecimal.valueOf(1000));
        }
        MenuDto menuDto = new MenuDto(menu.getId(), menu.getName(), BigDecimal.valueOf(10000), menu.getMenuGroupId(), menu.getMenuProducts());

        assert menuProducts.size() != 10;

        // when & then
        assertThatThrownBy(() -> menuService.create(menuDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_상품_가격의_합이_메뉴의_가격과_같으면_예외를_던지지_않는다() {
        // given
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setQuantity(1L);
            product = new Product(product.getId(), product.getName(), BigDecimal.valueOf(1000));
        }
        MenuDto menuDto = new MenuDto(menu.getId(), menu.getName(), BigDecimal.valueOf(1000), menu.getMenuGroupId(), menu.getMenuProducts());

        assert menuProducts.size() == 1;

        // when & then
        assertThatCode(() -> menuService.create(menuDto))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴_상품_가격의_합이_메뉴의_가격보다_작으면_예외를_던지지_않는다() {
        // given
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setQuantity(1L);
            product = new Product(product.getId(), product.getName(), BigDecimal.valueOf(1000));
        }
        MenuDto menuDto = new MenuDto(menu.getId(), menu.getName(), BigDecimal.valueOf(500), menu.getMenuGroupId(), menu.getMenuProducts());

        assert menuProducts.size() == 1;

        // when & then
        assertThatCode(() -> menuService.create(menuDto))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴를_전체_조회한다() {
        Menu menu2 = new Menu(2L, "menu2", BigDecimal.valueOf(4000), null, null);

        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setSeq(1L);
        menuProduct2.setMenuId(2L);
        menuProduct2.setQuantity(1L);
        menuProduct2.setProductId(2L);

        given(menuDao.findAll())
                .willReturn(List.of(menu, menu2));
        given(menuProductDao.findAllByMenuId(menu2.getId()))
                .willReturn(List.of(menuProduct2));

        // when
        List<MenuDto> result = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(2);
            softly.assertThat(result.get(0).getName()).isEqualTo(menu.getName());
            softly.assertThat(result.get(0).getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(result.get(1).getName()).isEqualTo(menu2.getName());
            softly.assertThat(result.get(1).getPrice()).isEqualTo(menu2.getPrice());
        });
    }
}
