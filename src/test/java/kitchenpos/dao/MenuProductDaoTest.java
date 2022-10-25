package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴_상품을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuProductDaoTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuProductDao menuProductDao;

    @Test
    void 메뉴_상품을_저장하면_seq가_채워진다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.ZERO))
                .getId();
        MenuProduct menuProduct = 메뉴_상품을_생성한다(menuId, productId, 1);

        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        assertAll(
                () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
                () -> assertThat(savedMenuProduct).usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(menuProduct)
        );
    }

    @Test
    void id로_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.ZERO))
                .getId();
        MenuProduct menuProduct = menuProductDao.save(메뉴_상품을_생성한다(menuId, productId, 1));

        MenuProduct actual = menuProductDao.findById(menuProduct.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuProduct);
    }

    @Test
    void 없는_메뉴_상품_id로_조회하면_Optional_empty를_반환한다() {
        Optional<MenuProduct> actual = menuProductDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_메뉴_상품을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId1 = menuDao.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long menuId2 = menuDao.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long productId1 = productDao.save(상품을_생성한다("상품1", BigDecimal.ZERO))
                .getId();
        Long productId2 = productDao.save(상품을_생성한다("상품2", BigDecimal.ZERO))
                .getId();
        MenuProduct menuProduct1 = menuProductDao.save(메뉴_상품을_생성한다(menuId1, productId1, 1));
        MenuProduct menuProduct2 = menuProductDao.save(메뉴_상품을_생성한다(menuId2, productId2, 2));

        List<MenuProduct> actual = menuProductDao.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(menuProduct1, menuProduct2);
    }

    @Test
    void 메뉴_id에_해당하는_모든_메뉴_상품을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId1 = menuDao.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long menuId2 = menuDao.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, null))
                .getId();
        Long productId1 = productDao.save(상품을_생성한다("상품1", BigDecimal.ZERO))
                .getId();
        Long productId2 = productDao.save(상품을_생성한다("상품2", BigDecimal.ZERO))
                .getId();
        MenuProduct menuProduct1 = menuProductDao.save(메뉴_상품을_생성한다(menuId1, productId1, 1));
        menuProductDao.save(메뉴_상품을_생성한다(menuId2, productId2, 2));

        List<MenuProduct> actual = menuProductDao.findAllByMenuId(menuId1);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(menuProduct1);
    }
}
