package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private DataSupport dataSupport;

    @DisplayName("새로운 메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given, when
        final MenuGroupRequest request = RequestBuilder.ofMenuGroup();
        final MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final MenuGroup savedMenuGroup1 = dataSupport.saveMenuGroup("추천 메뉴");
        final MenuGroup savedMenuGroup2 = dataSupport.saveMenuGroup("할인 메뉴");

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(savedMenuGroup1, savedMenuGroup2));
    }
}
