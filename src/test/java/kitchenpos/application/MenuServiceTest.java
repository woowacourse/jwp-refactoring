package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("list: 전체 메뉴 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_100_000_won = createMenuProduct(null, product_20_000_won.getId(), 5);
        Menu menu_with_price_is_80_000_won = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_100_000_won));
        menuService.create(menu_with_price_is_80_000_won);

        final List<Menu> menuGroups = menuService.list();

        assertThat(menuGroups).hasSize(1);
    }

    @DisplayName("create: 새 메뉴 등록 요청시, 메뉴 등록 후, 등록한 신 메뉴 객체를 반환한다.")
    @Test
    void create() {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_100_000_won = createMenuProduct(null, product_20_000_won.getId(), 5);
        Menu menu_with_price_is_80_000_won = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_100_000_won));

        final Menu savedMenu = menuService.create(menu_with_price_is_80_000_won);
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getMenuGroupId()).isNotNull(),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1),
                () -> assertThat(savedMenu.getName()).isEqualTo("후라이드 5마리 세트"),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(80_000))
        );
    }

    @DisplayName("create: 가격이 음수인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_negative() {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_5_ea_20_000_won = createMenuProduct(null, product_20_000_won.getId(), 5);
        Menu menu_price_is_negative = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(-16_000), setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_5_ea_20_000_won));

        assertThatThrownBy(() -> menuService.create(menu_price_is_negative))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 null인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_null() {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_5_ea_20_000_won = createMenuProduct(null, product_20_000_won.getId(), 5);
        Menu menu_price_is_null = createMenu("후라이드 5마리 세트", null, setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_5_ea_20_000_won));

        assertThatThrownBy(() -> menuService.create(menu_price_is_null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 소속 메뉴 그룹이 없는 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_group_non_exist() {
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_5_ea_20_000_won = createMenuProduct(null, product_20_000_won.getId(), 5);
        Menu menu_has_no_menu_group = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), null,
                Collections.singletonList(menuProduct_with_5_ea_20_000_won));

        assertThatThrownBy(() -> menuService.create(menu_has_no_menu_group))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 메뉴 가격이 구성 단품 가격의 합보다 큰 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_total_menu_product_price_is_bigger_than_menu_price() {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_10_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(10_000)));
        MenuProduct menuProduct_with_5_ea_10_000_won = createMenuProduct(null, product_10_000_won.getId(), 5);
        Menu menu_85_000_won = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(85_000), setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_5_ea_10_000_won));

        assertThatThrownBy(() -> menuService.create(menu_85_000_won))
                .isInstanceOf(IllegalArgumentException.class);
    }
}