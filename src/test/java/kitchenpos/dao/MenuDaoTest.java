package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class MenuDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuDao menuDao;

    @Test
    void 메뉴를_저장하면_id가_채워진다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu = 메뉴를_생성한다("메뉴", new BigDecimal(0), menuGroupId, null);

        Menu savedMenu = menuDao.save(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price")
                        .isEqualTo(menu)
        );
    }

    @Test
    void id로_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu = menuDao.save(메뉴를_생성한다("메뉴", new BigDecimal(0), menuGroupId, null));

        Menu actual = menuDao.findById(menu.getId())
                .orElseGet(Assertions::fail);

        assertAll(
                () -> assertThat(actual.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("price")
                        .isEqualTo(menu)
        );
    }

    @Test
    void 없는_메뉴_id로_조회하면_Optional_empty를_반환한다() {
        Optional<Menu> actual = menuDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu1 = menuDao.save(메뉴를_생성한다("메뉴1", new BigDecimal(0), menuGroupId, null));
        Menu menu2 = menuDao.save(메뉴를_생성한다("메뉴2", new BigDecimal(0), menuGroupId, null));

        List<Menu> actual = menuDao.findAll();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("price")
                .containsExactly(menu1, menu2);
    }

    @Test
    void id_목록에_있는_메뉴의_개수를_셀_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu1 = menuDao.save(메뉴를_생성한다("메뉴1", new BigDecimal(0), menuGroupId, null));
        Menu menu2 = menuDao.save(메뉴를_생성한다("메뉴2", new BigDecimal(0), menuGroupId, null));
        List<Long> ids = Arrays.asList(menu1.getId(), menu2.getId());

        long count = menuDao.countByIdIn(ids);

        assertThat(count).isEqualTo(ids.size());
    }
}