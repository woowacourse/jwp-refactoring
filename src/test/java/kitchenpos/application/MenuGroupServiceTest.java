package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService sut;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹을_등록한다() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("피자");

        // when
        MenuGroupResponse result = sut.create(request);

        // then
        assertThat(menuGroupDao.findById(result.getId())).isPresent();
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup pizzaGroup = menuGroupDao.save(메뉴_그룹("피자"));
        MenuGroup chickenGroup = menuGroupDao.save(메뉴_그룹("치킨"));

        // when
        List<MenuGroupResponse> result = sut.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MenuGroupResponse.from(pizzaGroup), MenuGroupResponse.from(chickenGroup)));
    }
}
