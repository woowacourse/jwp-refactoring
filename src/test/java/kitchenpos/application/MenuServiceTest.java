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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.management.ThreadDumpEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.util.Assert.isInstanceOf;

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
        menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("한마리메뉴");

        menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("menu");
        menu.setPrice(new BigDecimal("2000"));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal("1000"));

        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        given(productDao.findById(menuProduct.getProductId()))
                .willReturn(Optional.of(product));
        given(menuDao.save(menu))
                .willReturn(menu);
        given(menuProductDao.save(menuProduct))
                .willReturn(menuProduct);
    }

    @Test
    void 메뉴를_생성한다() {
        // given & when
        Menu result = menuService.create(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(menu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        });
    }

    @Test
    void 가격이_0이하인_메뉴를_생성하면_예외를_던진다() {
        // given
        menu.setPrice(new BigDecimal("0"));
        product.setPrice(new BigDecimal("0"));

        // when
        Menu result = menuService.create(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(menu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        });
    }

    @Test
    void 메뉴_상품_가격의_합과_상품_가격의_합이_다르면_예외를_던진다() {
        // given
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setQuantity(1L);
            product.setPrice(BigDecimal.valueOf(1000));
        }
        menu.setPrice(BigDecimal.valueOf(10000));

        assert menuProducts.size() != 10;

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_전체_조회한다() {
        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("menu2");
        menu2.setPrice(new BigDecimal("4000"));

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
        List<Menu> result = menuService.list();

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
