package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성_요청;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_응답;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.repositroy.MenuGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest implements ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_생성_할_수_있다() {
        // given
        final MenuGroupCreateRequest menuGroupCreateRequest = 메뉴_그룹_생성_요청("메뉴 그룹");

        // when
        final MenuGroupResponse created = menuGroupService.create(menuGroupCreateRequest);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(created.getId()).isNotNull();
            softly.assertThat(created.getName()).isEqualTo("메뉴 그룹");
        });
    }

    @Test
    void 메뉴_그룹을_조회할_수_있다() {
        // given
        final MenuGroup menuGroup1 = menuGroupRepository.save(메뉴_그룹("메뉴 그룹1"));
        final MenuGroup menuGroup2 = menuGroupRepository.save(메뉴_그룹("메뉴 그룹2"));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                    메뉴_그룹_응답(menuGroup1),
                    메뉴_그룹_응답(menuGroup2)
        ));
    }
}
