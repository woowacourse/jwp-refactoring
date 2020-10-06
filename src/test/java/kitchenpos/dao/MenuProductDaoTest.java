package kitchenpos.dao;

import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuProductDaoTest extends DaoTest {

    @Autowired
    private MenuProductDao menuProductDao;

    private Long menuId;
    private Long productId;
    private Long quantity;

    @BeforeEach
    void setUp() {
        menuId = 1L;
        productId = 1L;
        quantity = 1L;
    }

    @DisplayName("메뉴 상품 save - 성공")
    @Test
    void save() {
        MenuProduct menuProduct = createMenuProduct(menuId, productId, quantity);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        assertAll(() -> {
            assertThat(savedMenuProduct.getSeq()).isNotNull();
            assertThat(savedMenuProduct.getProductId()).isEqualTo(productId);
            assertThat(savedMenuProduct.getQuantity()).isEqualTo(quantity);
        });
    }

    @DisplayName("메뉴 상품 findById - 성공")
    @Test
    void findById() {
        MenuProduct menuProduct = createMenuProduct(menuId, productId, quantity);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);
        Optional<MenuProduct> foundMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq());

        assertThat(foundMenuProduct.isPresent()).isTrue();
    }

    @DisplayName("메뉴 상품 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<MenuProduct> foundMenuProduct = menuProductDao.findById(NOT_EXIST_VALUE);

        assertThat(foundMenuProduct.isPresent()).isFalse();
    }

    @DisplayName("메뉴 상품 findAll - 성공")
    @Test
    void findAll() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertThat(menuProducts).hasSize(6);
    }

    @DisplayName("메뉴 상품 findAllByMenuId - 성공")
    @Test
    void findAllByMenuId() {
        assertThat(menuProductDao.findAllByMenuId(1L)).hasSize(1);
    }
}
