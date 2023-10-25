package kitchenpos.ui.v1.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableUpdateRequest;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductUpdateRequest;
import kitchenpos.support.DatabaseCleanupExtension;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(value = DatabaseCleanupExtension.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public abstract class V1IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ResultActions 주문_테이블_생성(OrderTableCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 주문_테이블_모두_조회() throws Exception {
        return mockMvc.perform(get("/api/v1/tables")
            .contentType(MediaType.APPLICATION_JSON));
    }

    ResultActions 주문_테이블_빈_테이블_변경(Long orderTableId, OrderTableUpdateRequest request) throws Exception {
        return mockMvc.perform(put("/api/v1/tables/{id}/empty", orderTableId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 주문_테이블_방문자_변경(Long orderTableId, OrderTableUpdateRequest request) throws Exception {
        return mockMvc.perform(put("/api/v1/tables/{id}/number-of-guests", orderTableId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 상품_생성(ProductCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 상품_수정(Long productId, ProductUpdateRequest request) throws Exception {
        return mockMvc.perform(patch("/api/v1/products/{id}", productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 모든_상품_조회() throws Exception {
        return mockMvc.perform(get("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON));
    }

    ResultActions 메뉴_그룹_생성(MenuGroupCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 메뉴_생성(MenuCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 테이블_그룹_생성(TableGroupCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 테이블_그룹_해제(Long tableGroupId) throws Exception {
        return mockMvc.perform(delete("/api/v1/table-groups/{id}", tableGroupId));
    }

    ResultActions 주문_생성(OrderCreateRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 주문_상태_변경(Long orderId, OrderUpdateRequest request) throws Exception {
        return mockMvc.perform(put("/api/v1/orders/{id}/order-status", orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    }

    ResultActions 모든_주문_조회() throws Exception {
        return mockMvc.perform(get("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON));
    }
}
