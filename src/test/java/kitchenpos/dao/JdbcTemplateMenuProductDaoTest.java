package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴상품Dao 테스트")
class JdbcTemplateMenuProductDaoTest extends DomainDaoTest {
    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @DisplayName("메뉴상품을 등록한다.")
    @Test
    void save() {
        // given
        long menuId = SAVE_MENU_RETURN_ID();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setMenuId(menuId);

        // when
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // then
        assertThat(savedMenuProduct.getSeq()).isNotNull();
        assertThat(savedMenuProduct.getProductId()).isEqualTo(menuProduct.getProductId());
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(menuProduct.getQuantity());
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(menuProduct.getMenuId());
    }

    @DisplayName("id로 메뉴상품을 조회한다.")
    @Test
    void findById() {
        // given
        long menuId = SAVE_MENU_RETURN_ID();
        long menuProductId = SAVE_MENU_PRODUCT(menuId);

        // when
        Optional<MenuProduct> findMenuProduct = menuProductDao.findById(menuProductId);

        // then
        assertThat(findMenuProduct).isPresent();
        MenuProduct menuProduct = findMenuProduct.get();
        assertThat(menuProduct.getSeq()).isEqualTo(menuProductId);
    }

    @DisplayName("모든 메뉴상품을 조회한다.")
    @Test
    void findAll() {
        // given
        long menuId = SAVE_MENU_RETURN_ID();
        SAVE_MENU_PRODUCT(menuId);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 6개 + 새로 추가한 메뉴 1개
        assertThat(menuProducts).hasSize(6 + 1);
    }

    @DisplayName("menuId에 해당하는 모든 메뉴상품을 조회한다.")
    @Test
    void findAllByMenuId() {
        // given
        long menuId = SAVE_MENU_RETURN_ID();
        SAVE_MENU_PRODUCT(menuId);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);

        // then
        assertThat(menuProducts).hasSize(1);
    }

    private long SAVE_MENU_PRODUCT(long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setMenuId(menuId);

        // when
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);
        return savedMenuProduct.getSeq();
    }
}
