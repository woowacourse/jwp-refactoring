package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateMenuDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴를_저장한다() {
        // given
        Product product = productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = 메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of());

        // when
        Menu result = jdbcTemplateMenuDao.save(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getMenuGroupId()).isEqualTo(1L);
            softly.assertThat(result.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
            softly.assertThat(result.getName()).isEqualTo(menu.getName());
        });
    }

    @Test
    void 메뉴를_id로_찾는다() {
        // given
        productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        jdbcTemplateMenuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));

        // when
        Optional<Menu> result = jdbcTemplateMenuDao.findById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(1L);
        });
    }

    @Test
    void 모든_메뉴를_찾는다() {
        // given
        productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        jdbcTemplateMenuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));

        // when
        List<Menu> result = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void id가_들어간_메뉴의_수를_조회한다() {
        // given
        productDao.save(상품_생성_10000원());
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        jdbcTemplateMenuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));

        // when
        long size = jdbcTemplateMenuDao.countByIdIn(List.of(1L, 2L));

        // then
        assertThat(size).isEqualTo(1);
    }
}
