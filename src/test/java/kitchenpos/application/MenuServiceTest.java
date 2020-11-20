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

@SuppressWarnings("NonAsciiCharacters")
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
        MenuGroup 세트그룹 = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product 후라이드_2만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct 후라이드_5마리_10만원 = createMenuProduct(null, 후라이드_2만원.getId(), 5);
        Menu 후라이드_5마리세트_8만원 = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), 세트그룹.getId(),
                Collections.singletonList(후라이드_5마리_10만원));
        menuService.create(후라이드_5마리세트_8만원);

        final List<Menu> 전체메뉴목록 = menuService.list();

        assertThat(전체메뉴목록).hasSize(1);
    }

    @DisplayName("create: 새 메뉴 등록 요청시, 메뉴 등록 후, 등록한 신 메뉴 객체를 반환한다.")
    @Test
    void create() {
        MenuGroup 세트그룹 = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product 후라이드_2만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct 후라이드_5마리_10만원 = createMenuProduct(null, 후라이드_2만원.getId(), 5);
        Menu 후라이드_5마리세트_8만원 = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), 세트그룹.getId(),
                Collections.singletonList(후라이드_5마리_10만원));

        final Menu 추가완료된_후라이드_5마리세트_8만원 = menuService.create(후라이드_5마리세트_8만원);
        assertAll(
                () -> assertThat(추가완료된_후라이드_5마리세트_8만원.getId()).isNotNull(),
                () -> assertThat(추가완료된_후라이드_5마리세트_8만원.getMenuGroupId()).isNotNull(),
                () -> assertThat(추가완료된_후라이드_5마리세트_8만원.getMenuProducts()).hasSize(1),
                () -> assertThat(추가완료된_후라이드_5마리세트_8만원.getName()).isEqualTo("후라이드 5마리 세트"),
                () -> assertThat(추가완료된_후라이드_5마리세트_8만원.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(80_000))
        );
    }

    @DisplayName("create: 가격이 음수인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_negative() {
        MenuGroup 세트그룹 = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product 후라이드_2만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct 후라이드_5마리_10만원 = createMenuProduct(null, 후라이드_2만원.getId(), 5);
        Menu 후라이드_5마리세트_음수가격 = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(-16_000), 세트그룹.getId(),
                Collections.singletonList(후라이드_5마리_10만원));

        assertThatThrownBy(() -> menuService.create(후라이드_5마리세트_음수가격))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 null인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_null() {
        MenuGroup 세트그룹 = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product 후라이드_2만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct 후라이드_5마리_10만원 = createMenuProduct(null, 후라이드_2만원.getId(), 5);
        Menu 후라이드_5마리세트_null가격 = createMenu("후라이드 5마리 세트", null, 세트그룹.getId(),
                Collections.singletonList(후라이드_5마리_10만원));

        assertThatThrownBy(() -> menuService.create(후라이드_5마리세트_null가격))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 소속 메뉴 그룹이 없는 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_group_non_exist() {
        Product 후라이드_2만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct 후라이드_5마리_10만원 = createMenuProduct(null, 후라이드_2만원.getId(), 5);
        Menu 메뉴그룹이없는_후라이드_5마리세트 = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(80_000), null,
                Collections.singletonList(후라이드_5마리_10만원));

        assertThatThrownBy(() -> menuService.create(메뉴그룹이없는_후라이드_5마리세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 메뉴 가격이 구성 단품 가격의 합보다 큰 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_total_menu_product_price_is_bigger_than_menu_price() {
        MenuGroup 세트그룹 = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product 후라이드_1만원 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(10_000)));
        MenuProduct 후라이드_5마리_5만원 = createMenuProduct(null, 후라이드_1만원.getId(), 5);
        Menu 후라이드_5마리세트_8만5천원 = createMenu("후라이드 5마리 세트", BigDecimal.valueOf(85_000), 세트그룹.getId(),
                Collections.singletonList(후라이드_5마리_5만원));

        assertThatThrownBy(() -> menuService.create(후라이드_5마리세트_8만5천원))
                .isInstanceOf(IllegalArgumentException.class);
    }
}