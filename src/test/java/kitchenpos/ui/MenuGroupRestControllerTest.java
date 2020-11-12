package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MenuGroupRestControllerTest {
    private static final String API = "/api";
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private MenuGroupService menuGroupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("'/menu-groups'로 POST 요청 시, 메뉴 그룹을 생성한다")
    @Test
    void createTest() throws Exception {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(메뉴_그룹_이름_후라이드_세트);
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        when(menuGroupService.create(any(MenuGroupCreateRequest.class))).thenReturn(menuGroupResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuGroupCreateRequest);

        this.mockMvc.perform(post(API + "/menu-groups").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 MenuGroup 생성 요청의 이름이 유효하지 않으면, 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void createWithCreateRequestInvalidNameExceptionTest(String invalidName) throws Exception {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(invalidName);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuGroupCreateRequest);

        this.mockMvc.perform(post(API + "/menu-groups").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/menu-groups'로 GET 요청 시, 상품의 목록을 반환한다.")
    @Test
    void listTest() throws Exception {
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(menuGroupResponse);

        when(menuGroupService.list()).thenReturn(menuGroupResponses);

        this.mockMvc.perform(get(API + "/menu-groups").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].id").value(메뉴_그룹_ID_1)).
                andExpect(jsonPath("$[0].name").value(메뉴_그룹_이름_후라이드_세트));
    }
}
