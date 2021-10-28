package kitchenpos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import static kitchenpos.application.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        menuGroupService.create(MENU_GROUP);
        productService.create(PRODUCT);
    }

    @Test
    @DisplayName("메뉴 생성")
    void createTest() {

        // when
        menuService.create(MENU);

        // then
        assertThat(menuDao.findById(1L).get()).isEqualTo(MENU);
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void listTest() {

        // given
        menuService.create(MENU);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus).contains(MENU);
    }
}
