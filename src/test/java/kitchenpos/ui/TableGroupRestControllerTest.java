package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
class TableGroupRestControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("create: 2개 이상의 중복되지 않고 비어있지 않는 테이블목록에 대해 테이블 그룹 지정 요청시, 201 반환과 함께 그룹 지정된 테이블 그룹을 반환한다.")
    @Test
    void create() throws Exception {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(5);
        final OrderTable orderTable = tableService.create(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(10);
        tableService.create(secondTable);
        final OrderTable orderTable2 = tableService.create(secondTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(orderTable, orderTable2));

        String url = "/api/table-groups";

        mockMvc.perform(post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.createdDate", notNullValue(LocalDateTime.class)))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @DisplayName("ungroup: 테이블 그룹 대상에 포함되어있는 테이블 모두 주문 완료 상태인 경우, 해당 테이블들의 테이블 그룹 해지 후 204 코드를 반환한다.")
    @Test
    void ungroup() throws Exception {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        final OrderTable orderTable = tableService.create(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        tableService.create(secondTable);
        final OrderTable orderTable2 = tableService.create(secondTable);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(orderTable, orderTable2));
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        final Long savedTableGroupId = createdTableGroup.getId();

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        final Order order1 = orderService.create(order);

        final Order order2 = new Order();
        order2.setOrderTableId(orderTable2.getId());
        order2.setOrderLineItems(Collections.singletonList(orderLineItem));
        final Order order4 = orderService.create(order2);

        final Order order3 = new Order();
        order3.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(order1.getId(), order3);
        orderService.changeOrderStatus(order4.getId(), order3);

        String url = "/api/table-groups/{tableGroupId}";

        mockMvc.perform(delete(url, savedTableGroupId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}