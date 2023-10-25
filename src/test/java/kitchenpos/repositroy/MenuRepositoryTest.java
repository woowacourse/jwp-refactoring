package kitchenpos.repositroy;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.ProductFixture.상품;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnitUtil;
import kitchenpos.config.QuerydslConfig;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuRepositoryTest {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupRepository.save(메뉴_그룹("메뉴 그룹"));

        this.product1 = productRepository.save(상품("후라이드", BigDecimal.valueOf(1000L)));
        this.product2 = productRepository.save(상품("양념", BigDecimal.valueOf(1000L)));

        this.menuProduct1 = 메뉴_상품(product1, 1L);
        this.menuProduct2 = 메뉴_상품(product2, 2L);

        menuRepository.save(메뉴("메뉴", 3000L, 메뉴_상품들(menuProduct1, menuProduct2), menuGroup));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 메뉴_전체_조회_시_한번에_가져_온다() {
        // given
        final PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();

        // when
        final List<Menu> result = menuRepository.findAllByFetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(persistenceUnitUtil.isLoaded(product1)).isTrue();
            softly.assertThat(persistenceUnitUtil.isLoaded(product2)).isTrue();
            softly.assertThat(persistenceUnitUtil.isLoaded(menuProduct1)).isTrue();
            softly.assertThat(persistenceUnitUtil.isLoaded(menuProduct2)).isTrue();
            softly.assertThat(persistenceUnitUtil.isLoaded(menuGroup)).isTrue();
        });
    }
}
