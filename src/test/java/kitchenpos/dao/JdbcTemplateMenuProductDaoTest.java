package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static kitchenpos.testutils.TestDomainBuilder.menuProductBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JdbcTemplateMenuProductDao.class)
class JdbcTemplateMenuProductDaoTest extends AbstractJdbcTemplateDaoTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Autowired
    private MenuProductDao menuProductDao;

    private final Long friedChickenMenuId = 1L;
    private final Long friedChickenProductId = 1L;
    private final Long seasoningChickenMenuId = 2L;
    private final Long seasoningChickenProductId = 2L;

    private MenuProduct friedChickenMenuProduct, seasoningChickenMenuProduct;

    @BeforeEach
    void setUp() {
        turnOffReferentialIntegrity();

        friedChickenMenuProduct = menuProductDao.save(
                menuProductBuilder()
                        .menuId(friedChickenMenuId)
                        .productId(friedChickenProductId)
                        .quantity(1)
                        .build()
        );
        seasoningChickenMenuProduct = menuProductDao.save(
                menuProductBuilder()
                        .menuId(seasoningChickenMenuId)
                        .productId(seasoningChickenProductId)
                        .quantity(1)
                        .build()
        );
    }

    @AfterAll
    void afterAll() {
        turnOnReferentialIntegrity();
    }

    @DisplayName("메뉴 상품을 저장한다.")
    @Test
    void save() {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .menuId(friedChickenMenuId)
                .productId(friedChickenProductId)
                .quantity(2)
                .build();

        // when
        MenuProduct menuProduct = menuProductDao.save(newMenuProduct);

        // then
        assertThat(menuProduct.getSeq()).isNotNull();
        assertThat(menuProduct)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(newMenuProduct);
    }

    @DisplayName("존재하는 메뉴 상품을 조회한다.")
    @Test
    void findByIdWhenExistent() {
        // given
        Long id = friedChickenMenuProduct.getSeq();

        // when
        Optional<MenuProduct> optionalMenuProduct = menuProductDao.findById(id);

        // then
        assertThat(optionalMenuProduct).get()
                .usingRecursiveComparison()
                .isEqualTo(friedChickenMenuProduct);
    }

    @DisplayName("존재하지 않는 메뉴 상품을 조회한다.")
    @Test
    void findByIdWhenNonexistent() {
        // when
        Optional<MenuProduct> optionalMenuProduct = menuProductDao.findById(NON_EXISTENT_ID);

        // then
        assertThat(optionalMenuProduct).isEmpty();
    }

    @DisplayName("전체 메뉴 상품을 가져온다.")
    @Test
    void findAll() {
        // when
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        for (MenuProduct menuProduct : menuProducts) {
            System.out.println(menuProduct.getMenuId());
        }
        // then
        assertThat(menuProducts)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(friedChickenMenuProduct, seasoningChickenMenuProduct);
    }

    @DisplayName("특정 메뉴ID의 메뉴 상품 리스트를 가져온다.")
    @Test
    void findAllByMenuId() {
        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(seasoningChickenMenuId);

        // then
        assertThat(menuProducts)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(seasoningChickenMenuProduct);
    }
}
