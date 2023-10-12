package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

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

    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;
    private MenuProduct menuProduct;

    @BeforeEach
    void beforeEach() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");

        product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(new BigDecimal(1000));

        menu = new Menu();
        menu.setPrice(new BigDecimal(100));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName("menu");

        menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);
        menuProduct.setSeq(1L);

        menu.setMenuProducts(List.of(menuProduct));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        //given
        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(product.getId()))
                .willReturn(Optional.of(product));
        given(menuDao.save(menu))
                .willReturn(menu);
        given(menuProductDao.save(menuProduct))
                .willReturn(menuProduct);

        //when
        final Menu actual = menuService.create(menu);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual).isEqualTo(menu);
        });
    }

    @DisplayName("메뉴의 가격이 음수면 예외를 던진다.")
    @Test
    void 메뉴_가격이_0미만이면_예외() {
        //given
        menu.setPrice(new BigDecimal(-100));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 null이면 예외를 던진다.")
    @Test
    void 메뉴_가격이_null이면_예외() {
        //given
        menu.setPrice(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product들의 price합이 menu의 price미만이면 예외를 던진다.")
    @Test
    void price가_product의_price합_초과면_예외() {
        //given
        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(product.getId()))
                .willReturn(Optional.of(product));
        menu.setPrice(new BigDecimal(1000));
        product.setPrice(new BigDecimal(500));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("menu를 전체 조회한다.")
    @Test
    void menu_전체_조회() {
        //given
        given(menuDao.findAll())
                .willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(menu.getId()))
                .willReturn(List.of(menuProduct));

        //when
        final List<Menu> actual = menuService.list();

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(1);
            softly.assertThat(actual.get(0)).isEqualTo(menu);
        });
    }
}
