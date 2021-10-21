package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴Dao 테스트")
class JdbcTemplateMenuDaoTest extends DomainDaoTest {

    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @DisplayName("메뉴를 저장한다.")
    @Test
    void save() {
        // given
        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        // when - then
        Menu savedMenu = menuDao.save(menu);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }

    @DisplayName("id로 메뉴를 조회한다.")
    @Test
    void findById() {
        // given
        long id = SAVE_MENU_RETURN_ID();

        // when
        Optional<Menu> findMenu = menuDao.findById(id);

        // then
        assertThat(findMenu).isPresent();
        Menu menu = findMenu.get();
        assertThat(menu.getId()).isEqualTo(id);
    }

    @DisplayName("등록된 모든 메뉴를 조회한다.")
    @Test
    void findAll() {
        // given
        SAVE_MENU_RETURN_ID();

        // when
        List<Menu> menus = menuDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 6개 + 새로 추가한 메뉴 1개
        assertThat(menus).hasSize(6 + 1);
    }

    @DisplayName("Id에 해당하는 메뉴의 개수를 센다.")
    @Test
    void countByIdIn() {
        // given
        List<Long> ids = Arrays.asList(SAVE_MENU_RETURN_ID(), SAVE_MENU_RETURN_ID());

        // when
        long count = menuDao.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(2);
    }
}
