package kitchenpos.application;

import static kitchenpos.helper.MenuGroupHelper.*;
import static kitchenpos.helper.MenuHelper.*;
import static kitchenpos.helper.ProductHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Sql("/truncate.sql")
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup 메뉴_그룹;
    private Product 프라이드_상품;
    private MenuProduct 메뉴_상품;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupDao.save(createMenuGroup("메뉴그룹"));
        프라이드_상품 = productDao.save(createProduct("프라이드", BigDecimal.valueOf(15_000L)));
        메뉴_상품 = createMenuProduct(1L, null, 프라이드_상품.getId(), 2L);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        Menu 생성할_메뉴 = createMenu("프라이드+프라이드", BigDecimal.valueOf(30_000L), 메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품));

        // when
        Menu 메뉴 = menuService.create(생성할_메뉴);

        // then
        assertAll(
                () -> assertThat(메뉴.getId()).isNotNull(),
                () -> assertThat(메뉴.getMenuGroupId()).isEqualTo(생성할_메뉴.getMenuGroupId()),
                () -> assertThat(메뉴.getName()).isEqualTo(생성할_메뉴.getName()),
                () -> assertThat(메뉴.getPrice().longValueExact()).isEqualTo(생성할_메뉴.getPrice().longValueExact()),
                () -> assertThat(메뉴.getMenuProducts().get(0).getSeq()).isNotNull(),
                () -> assertThat(메뉴.getMenuProducts().get(0).getMenuId()).isEqualTo(메뉴_상품.getMenuId()),
                () -> assertThat(메뉴.getMenuProducts().get(0).getProductId()).isEqualTo(메뉴_상품.getProductId()),
                () -> assertThat(메뉴.getMenuProducts().get(0).getQuantity()).isEqualTo(메뉴_상품.getQuantity())
        );
    }

    @DisplayName("메뉴 가격이 0원보다 작은 경우 예외가 발생한다.")
    @Test
    void create_MenuPriceUnderZero_ExceptionThrown() {
        // given
        Menu 생성할_메뉴 = createMenu("프라이드+프라이드", BigDecimal.valueOf(-1), 메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(생성할_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 메뉴 그룹에 속하지 않으면 예외가 발생한다.")
    @Test
    void create_MenuGroupIsNull_ExceptionThrown() {
        // given
        Menu 생성할_메뉴 = createMenu("프라이드+프라이드", BigDecimal.valueOf(30_000L), null,
                Collections.singletonList(메뉴_상품));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(생성할_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품의 합보다 크면 예외가 발생한다.")
    @Test
    void create_MenuPriceOverMenuProductsSum_ExceptionThrown() {
        // given
        Menu 생성할_메뉴 = createMenu("프라이드+프라이드", BigDecimal.valueOf(30_001L), 메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(생성할_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Menu 생성할_메뉴_1 = createMenu("프라이드+프라이드", BigDecimal.valueOf(30_000L), 메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품));
        Menu 생성할_메뉴_2 = createMenu("프라이드+프라이드_2", BigDecimal.valueOf(30_000L), 메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품));
        Menu 메뉴_1 = menuService.create(생성할_메뉴_1);
        Menu 메뉴_2 = menuService.create(생성할_메뉴_1);

        // when
        List<Menu> 메뉴_목록 = menuService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_목록.get(0).getId()).isEqualTo(메뉴_1.getId()),
                () -> assertThat(메뉴_목록.get(1).getId()).isEqualTo(메뉴_2.getId())
        );
    }
}
