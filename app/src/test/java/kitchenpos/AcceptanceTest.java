package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.ChangeEmptyTableRequest;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.ChangeTableGuestRequest;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.request.CreateProductRequest;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderTableRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 정상_플로우_검증() throws Exception {
        Long 상품_아이디 = 상품_등록();

        Long 메뉴_그룹_아이디 = 메뉴_그룹_등록();
        Long 메뉴_아이디 = 메뉴_등록(상품_아이디, 메뉴_그룹_아이디);

        Long 테이블_아이디1 = 테이블_생성();
        Long 테이블_아이디2 = 테이블_생성();
        Long 테이블_아이디3 = 테이블_생성();

        빈_테이블을_주문_테이블로_변경(테이블_아이디3);
        Long 테이블_그룹_아이디 = 테이블_그룹_생성(테이블_아이디1, 테이블_아이디2);

        Long 주문_아이디 = 주문(메뉴_아이디, 테이블_아이디1);
        주문_테이블_손님_수_설정(테이블_아이디1);
        주문_상태_변경(주문_아이디, OrderStatus.MEAL);
        주문_상태_변경(주문_아이디, OrderStatus.COMPLETION);

        테이블_그룹_해제(테이블_그룹_아이디);
        주문_테이블을_빈_테이블로_변경(테이블_아이디1);
    }

    private Long 테이블_그룹_생성(Long 테이블_아이디1, Long 테이블_아이디2) throws Exception {
        String 테이블_그룹_생성_요청 = objectMapper.writeValueAsString(
                new CreateTableGroupRequest(List.of(
                        new OrderTableRequest(테이블_아이디1),
                        new OrderTableRequest(테이블_아이디2)
                )));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/table-groups")
                        .content(테이블_그룹_생성_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/table-groups/", ""));
    }

    private Long 주문(Long 메뉴_아이디, Long 테이블_아이디) throws Exception {
        String 주문_요청 = objectMapper.writeValueAsString(
                new CreateOrderRequest(테이블_아이디, List.of(new OrderLineItemRequest(메뉴_아이디, 2L))));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .content(주문_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/orders/", ""));
    }

    private Long 상품_등록() throws Exception {
        String 상품_등록_요청 = objectMapper.writeValueAsString(
                new CreateProductRequest("뿌링클", BigDecimal.valueOf(20000L)));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .content(상품_등록_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/products/", ""));
    }

    private Long 메뉴_그룹_등록() throws Exception {
        String 메뉴_그룹_등록_요청 = objectMapper.writeValueAsString(
                new CreateMenuGroupRequest("치킨 세트"));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                        .content(메뉴_그룹_등록_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/menu-groups/", ""));
    }

    private Long 메뉴_등록(Long 상품_아이디, Long 메뉴_그룹_아이디) throws Exception {
        String 메뉴_등록_요청 = objectMapper.writeValueAsString(
                new CreateMenuRequest("아시안게임 우승 기념 치킨 세트(할인)", BigDecimal.valueOf(19000L), 메뉴_그룹_아이디,
                        List.of(new MenuProductRequest(상품_아이디, 1L))));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/menus")
                        .content(메뉴_등록_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/menus/", ""));
    }

    private Long 테이블_생성() throws Exception {
        String 테이블_생성_요청 = objectMapper.writeValueAsString(
                new CreateOrderTableRequest(0, true));

        MvcResult 요청_결과 = mockMvc.perform(MockMvcRequestBuilders.post("/api/tables")
                        .content(테이블_생성_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String LOCATION_헤더 = 요청_결과.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        return Long.parseLong(LOCATION_헤더.replace("/api/tables/", ""));
    }

    private void 빈_테이블을_주문_테이블로_변경(Long 테이블_아이디) throws Exception {
        String 주문_테이블_변경_요청 = objectMapper.writeValueAsString(
                new ChangeEmptyTableRequest(false));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tables/{orderTableId}/empty", 테이블_아이디)
                        .content(주문_테이블_변경_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void 주문_테이블_손님_수_설정(Long 테이블_아이디) throws Exception {
        String 손님_수_변경_요청 = objectMapper.writeValueAsString(
                new ChangeTableGuestRequest(2));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tables/{orderTableId}/number-of-guests", 테이블_아이디)
                        .content(손님_수_변경_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void 주문_상태_변경(Long 주문_아이디, OrderStatus 주문_상태) throws Exception {
        String 주문_상태_변경_요청 = objectMapper.writeValueAsString(
                new ChangeOrderStatusRequest(주문_상태.name()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{orderId}/order-status", 주문_아이디)
                        .content(주문_상태_변경_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void 테이블_그룹_해제(Long 테이블_그룹_아이디) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/table-groups/{tableGroupId}", 테이블_그룹_아이디))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private void 주문_테이블을_빈_테이블로_변경(Long 테이블_아이디) throws Exception {
        String 주문_테이블_변경_요청 = objectMapper.writeValueAsString(
                new ChangeEmptyTableRequest(true));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tables/{orderTableId}/empty", 테이블_아이디)
                        .content(주문_테이블_변경_요청)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
