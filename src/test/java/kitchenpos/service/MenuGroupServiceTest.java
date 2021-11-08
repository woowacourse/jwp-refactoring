package kitchenpos.service;

import kitchenpos.application.MenuGroupService;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴그룹 서비스 테스트")
class MenuGroupServiceTest extends ServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupRequest request;

    @BeforeEach
    void setUp() {
        String name = MenuGroupFixture.create().getName();

        request = new MenuGroupRequest(name);
    }

    @DisplayName("메뉴그룹 생성")
    @Test
    void create() {
        //given
        when(menuGroupRepository.save(any())).thenReturn(MenuGroupFixture.create());
        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(request);
        //then
        assertThat(menuGroupResponse.getId()).isNotNull();
        assertThat(menuGroupResponse.getName()).isEqualTo(request.getName());
    }

    @DisplayName("메뉴그룹 조회")
    @Test
    void findAll() {
        //given
        when(menuGroupRepository.save(any())).thenReturn(MenuGroupFixture.create());
        when(menuGroupRepository.findAll()).thenReturn(Collections.singletonList(MenuGroupFixture.create()));
        menuGroupService.create(request);
        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();
        //then
        assertThat(menuGroups).hasSize(1);
    }
}