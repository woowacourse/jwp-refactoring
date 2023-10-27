package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.presentation.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_등록한다() {
        final MenuGroupCreateRequest 메뉴_그룹_생성_요청 = new MenuGroupCreateRequest("메뉴 그룹");

        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(menuGroup.getId()).isNotNull();
            softly.assertThat(menuGroup.getName()).isEqualTo("메뉴 그룹");
        });
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        final MenuGroup 메뉴_그룹1 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹1"));
        final MenuGroup 메뉴_그룹2 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹2"));

        final List<MenuGroup> menuGroups = menuGroupService.findAll();

        assertThat(menuGroups).hasSize(2);
    }
}
