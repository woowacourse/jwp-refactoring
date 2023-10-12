package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateMenuProductDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 저장한다() {
        // given
        Product product = productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        MenuProduct menuProduct = 메뉴_상품_10개_생성(menu.getId(), product.getId());

        // when
        MenuProduct result = jdbcTemplateMenuProductDao.save(menuProduct);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getProductId()).isEqualTo(product.getId());
            softly.assertThat(result.getMenuId()).isEqualTo(menu.getId());
            softly.assertThat(result.getQuantity()).isEqualTo(menuProduct.getQuantity());
        });
    }

    @Test
    void 메뉴_아이디로_상품들을_조회한다() {
        // given
        Product product = productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        jdbcTemplateMenuProductDao.save(메뉴_상품_10개_생성(menu.getId(), product.getId()));

        // when
        List<MenuProduct> result = jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId());

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 모두_조회한다() {
        // given
        Product product = productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        jdbcTemplateMenuProductDao.save(메뉴_상품_10개_생성(menu.getId(), product.getId()));

        // when
        List<MenuProduct> result = jdbcTemplateMenuProductDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }
}
