package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_신메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        MenuGroup 두마리메뉴 = 메뉴그룹_두마리메뉴;

        // when
        MenuGroup created = menuGroupService.create(두마리메뉴);

        // then
        assertThat(menuGroupDao.findById(created.getId())).isPresent();
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        // given
        MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);
        MenuGroup 한마리메뉴 = menuGroupDao.save(메뉴그룹_한마리메뉴);
        MenuGroup 신메뉴 = menuGroupDao.save(메뉴그룹_신메뉴);

        // when
        List<MenuGroup> findList = menuGroupService.list();

        // then
        assertThat(findList).usingRecursiveComparison()
                .isEqualTo(List.of(두마리메뉴, 한마리메뉴, 신메뉴));
    }
}
