package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_생성_요청;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_신메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.MenuGroupDto;
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
        final var request = 메뉴그룹_생성_요청("두마리메뉴");

        // when
        final var response = menuGroupService.create(request);

        // then
        assertThat(menuGroupDao.findById(response.getId())).isPresent();
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        // given
        final var 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);
        final var 한마리메뉴 = menuGroupDao.save(메뉴그룹_한마리메뉴);
        final var 신메뉴 = menuGroupDao.save(메뉴그룹_신메뉴);
        final var 메뉴그룹목록 = List.of(두마리메뉴, 한마리메뉴, 신메뉴);

        final var expected = 메뉴그룹목록.stream()
                .map(MenuGroupDto::toDto)
                .collect(Collectors.toList());

        // when
        final var actual = menuGroupService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
