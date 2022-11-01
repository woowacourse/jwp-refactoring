package kitchenpos.domain.menu;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 메뉴를_저장하면_id가_채워진다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>());

        Menu savedMenu = menuRepository.save(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price")
                        .isEqualTo(menu)
        );
    }

    @Test
    void id로_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()));

        Menu actual = menuRepository.findById(menu.getId())
                .orElseGet(Assertions::fail);

        assertAll(
                () -> assertThat(actual.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("price")
                        .isEqualTo(menu)
        );
    }

    @Test
    void 없는_메뉴_id로_조회하면_Optional_empty를_반환한다() {
        Optional<Menu> actual = menuRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu1 = menuRepository.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, new ArrayList<>()));
        Menu menu2 = menuRepository.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, new ArrayList<>()));

        List<Menu> actual = menuRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("price")
                .containsExactly(menu1, menu2);
    }

    @Test
    void id_목록에_있는_메뉴의_개수를_셀_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹")).getId();
        Menu menu1 = menuRepository.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, new ArrayList<>()));
        Menu menu2 = menuRepository.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, new ArrayList<>()));
        List<Long> ids = List.of(menu1.getId(), menu2.getId());

        long count = menuRepository.countByIdIn(ids);

        assertThat(count).isEqualTo(ids.size());
    }
}
