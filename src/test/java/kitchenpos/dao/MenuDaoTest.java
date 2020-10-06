package kitchenpos.dao;

import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createMenu;
import static kitchenpos.utils.TestObjectUtils.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuDaoTest extends DaoTest {

    @Autowired
    private MenuDao menuDao;

    private String menuName;
    private BigDecimal menuPrice;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuName = "후라이드+후라이드";
        menuPrice = BigDecimal.valueOf(19000L);
        menuGroupId = 1L;
        menuProducts = Collections.singletonList(createMenuProduct(1L, 1L, 2L));
    }

    @DisplayName("메뉴 save - 성공")
    @Test
    void save() {
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);
        Menu savedMenu = menuDao.save(menu);

        assertAll(() -> {
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getPrice()).isEqualByComparingTo(menuPrice);
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroupId);
        });
    }

    @DisplayName("메뉴 findById - 성공")
    @Test
    void findById() {
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);
        Menu savedMenu = menuDao.save(menu);
        Optional<Menu> foundMenu = menuDao.findById(savedMenu.getId());

        assertThat(foundMenu.isPresent()).isTrue();
    }

    @DisplayName("메뉴 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<Menu> foundMenu = menuDao.findById(NOT_EXIST_VALUE);

        assertThat(foundMenu.isPresent()).isFalse();
    }

    @DisplayName("메뉴 findAll - 성공")
    @Test
    void findAll() {
        List<Menu> menus = menuDao.findAll();

        assertThat(menus).hasSize(6);
    }

    @DisplayName("메뉴 countByIdIn - 성공")
    @Test
    void countByIdIn() {
        List<Menu> menus = menuDao.findAll();
        List<Long> menuIds = menus.stream()
            .map(Menu::getId)
            .collect(Collectors.toList());
        long countMenus = menuDao.countByIdIn(menuIds);
        assertThat(countMenus).isEqualTo(6);
    }
}
