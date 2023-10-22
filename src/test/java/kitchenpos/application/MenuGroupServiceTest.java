package kitchenpos.application;

import static kitchenpos.application.MenuGroupServiceTest.MenuGroupRequestFixture.메뉴_그룹_생성_요청;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupCreateRequest request = 메뉴_그룹_생성_요청();

        // when
        MenuGroupResponse createdMenuGroup = menuGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdMenuGroup.getId()).isNotNull();
            softly.assertThat(createdMenuGroup).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(MenuGroupResponse.from(메뉴_그룹()));
        });
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId = menuGroupDao.save(메뉴_그룹()).getId();

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).usingRecursiveComparison()
                .isEqualTo(List.of(MenuGroupResponse.from(메뉴_그룹(menuGroupId))));
    }

    static class MenuGroupRequestFixture {

        public static MenuGroupCreateRequest 메뉴_그룹_생성_요청() {
            return new MenuGroupCreateRequest("menuGroupName");
        }
    }
}
