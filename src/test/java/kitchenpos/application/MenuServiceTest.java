package kitchenpos.application;

import kitchenpos.application.common.TestFixtureFactory;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.menuRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class MenuServiceTest extends TestFixtureFactory {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @DisplayName("메뉴 생성 기능 테스트")
    @Test
    void create() {
        menuRequest menuRequest = makeMenuCreateRequest("추천메뉴", "양념", 13000);

        MenuResponse menuResponse = menuService.create(menuRequest);

        List<MenuProductDto> savedMenuProducts = menuResponse.getMenuProducts();
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(savedMenuProducts.get(0).getId()).isEqualTo(menuResponse.getId())
        );
    }

    @DisplayName("메뉴 생성 - price가 null일 때 예외처리")
    @Test
    void createWhenNullPrice() {
        menuRequest menuRequest = new menuRequest("추천메뉴", null, 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price가 0 미만일 경우 예외처리")
    @Test
    void createWhenPriceLessZero() {
        menuRequest menuRequest = new menuRequest("추천메뉴", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price 구성품 가격의 합보다 큰 경우 예외처리")
    @Test
    void createWhenPriceGraterSum() {
        menuRequest menuRequest = new menuRequest("추천메뉴", BigDecimal.valueOf(90000), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회 기능 테스트")
    @Test
    void list() {
        menuService.create(makeMenuCreateRequest("추천메뉴", "양념", 13000));
        menuService.create(makeMenuCreateRequest("추천메뉴", "후라이드", 12000));
        assertAll(
                () -> assertThat(menuService.list()).hasSize(2)
        );
    }

    @AfterEach
    void tearDown() {
        menuProductDao.deleteAll();
        menuDao.deleteAll();
        productDao.deleteAll();
        menuGroupDao.deleteAll();
    }
}
