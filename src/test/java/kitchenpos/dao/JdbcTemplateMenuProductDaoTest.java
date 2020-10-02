package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.DomainFactory.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateMenuProductDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    @BeforeEach
    void setUp() {
        menuProductSeqs = new ArrayList<>();
        saveProducts(1L, "후라이드", BigDecimal.valueOf(16_000));
        saveProducts(2L, "양념치킨", BigDecimal.valueOf(16_000));
        saveMenuGroup(1L, "한마리메뉴");
        saveMenu(1L, "후라이드", BigDecimal.valueOf(16_000), 1L);
        saveMenu(2L, "양념치킨", BigDecimal.valueOf(16_000), 1L);
    }

    @DisplayName("메뉴 상품 저장")
    @Test
    void saveTest() {
        MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L);

        MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);
        menuProductSeqs.add(savedMenuProduct.getSeq());

        assertAll(
                () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
                () -> assertThat(savedMenuProduct.getMenuId()).isEqualTo(1L),
                () -> assertThat(savedMenuProduct.getProductId()).isEqualTo(1L),
                () -> assertThat(savedMenuProduct.getQuantity()).isEqualTo(1L)
        );
    }

    @DisplayName("seq 값에 해당하는 메뉴 상품 반환")
    @Test
    void findByIdTest() {
        MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L);
        MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

        MenuProduct findMenuProduct = jdbcTemplateMenuProductDao.findById(savedMenuProduct.getSeq()).get();
        menuProductSeqs.add(savedMenuProduct.getSeq());

        assertAll(
                () -> assertThat(findMenuProduct.getSeq()).isEqualTo(savedMenuProduct.getSeq()),
                () -> assertThat(findMenuProduct.getMenuId()).isEqualTo(savedMenuProduct.getMenuId()),
                () -> assertThat(findMenuProduct.getProductId()).isEqualTo(savedMenuProduct.getProductId()),
                () -> assertThat(findMenuProduct.getQuantity()).isEqualTo(savedMenuProduct.getQuantity())
        );
    }

    @DisplayName("존재하지 않는 seq 값 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidMenuProductSeqTest() {
        Optional<MenuProduct> findMenuProduct = jdbcTemplateMenuProductDao.findById(0L);

        assertThat(findMenuProduct).isEqualTo(Optional.empty());
    }

    @DisplayName("저장된 모든 메뉴 상품 반환")
    @Test
    void findAllTest() {
        MenuProduct friedMenuProduct = createMenuProduct(1L, 1L, 1L);
        MenuProduct sourceMenuProduct = createMenuProduct(2L, 2L, 1L);
        jdbcTemplateMenuProductDao.save(friedMenuProduct);
        jdbcTemplateMenuProductDao.save(sourceMenuProduct);

        List<MenuProduct> allMenuProducts = jdbcTemplateMenuProductDao.findAll();
        allMenuProducts.forEach(menuProduct -> menuProductSeqs.add(menuProduct.getSeq()));

        assertThat(allMenuProducts).hasSize(2);
    }

    @DisplayName("메뉴 아이디에 해당하는 메뉴 상품 반환")
    @Test
    void findAllByMenuIdTest() {
        MenuProduct menuProductWithQuantityOne = createMenuProduct(1L, 1L, 1L);
        MenuProduct menuProductWithQuantityTwo = createMenuProduct(1L, 1L, 2L);
        jdbcTemplateMenuProductDao.save(menuProductWithQuantityOne);
        jdbcTemplateMenuProductDao.save(menuProductWithQuantityTwo);

        List<MenuProduct> menuProductsWithMenuId = jdbcTemplateMenuProductDao.findAllByMenuId(1L);
        menuProductsWithMenuId.forEach(menuProduct -> menuProductSeqs.add(menuProduct.getSeq()));

        assertThat(menuProductsWithMenuId).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        deleteMenuProduct();
    }
}
