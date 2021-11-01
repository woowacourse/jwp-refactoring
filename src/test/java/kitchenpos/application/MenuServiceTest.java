package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("MenuService를 테스트한다.")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuProductDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
        productDao.deleteAll();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Arrays.asList(menuProduct));

        //when
        Menu result = menuService.create(menu);

        //then
        assertThat(result.getName()).isEqualTo("후라이드+후라이드");
        assertThat(result.getPrice().toBigInteger()).isEqualTo(BigDecimal.valueOf(19000).toBigInteger());
        assertThat(result.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(result.getMenuProducts().size()).isEqualTo(1);
        MenuProduct resultMenuProduct = result.getMenuProducts().get(0);
        assertThat(resultMenuProduct.getProductId()).isEqualTo(savedProduct.getId());
        assertThat(resultMenuProduct.getQuantity()).isEqualTo(2L);
    }

    @DisplayName("메뉴 등록시 메뉴 가격이 0원 이상이 아니면 예외를 던진다.")
    @Test
    void create_price_exception() {
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(-19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 메뉴 그룹이 존재하지 않으면 예외를 던진다.")
    @Test
    void create_menu_group_not_exist_exception() {
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 999L, Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 메뉴에 존재하지 않는 상품이 있다면 예외를 던진다.")
    @Test
    void create_product_not_exist_exception() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천 메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuProduct menuProduct = new MenuProduct(null, null, 999L, 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 메뉴 상품들의 가격 합계가 메뉴 가격보다 크다면 예외를 던진다.")
    @Test
    void create_product_price_sum_exception() {
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(50000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        //given
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        //when
        menuService.create(menu);
        menuService.create(menu);
        menuService.create(menu);

        //then
        List<Menu> result = menuService.list();
        assertThat(result.size()).isEqualTo(3);
    }

}