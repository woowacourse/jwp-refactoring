package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menuproduct.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class IntegrationTest {

    protected static final String LOCATION = "Location";
    protected static final String CONTENT_TYPE_NAME = "Content-type";
    protected static final String RESPONSE_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext ctx;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    protected String toJson(Object params) throws JsonProcessingException {
        return objectMapper.writeValueAsString(params);
    }

    protected void resetEntityManager() {
        entityManager.flush();
        entityManager.clear();
    }

    protected Product Product를_저장한다(String name, int price) {
        final Product product = new Product(name, price);
        return productRepository.save(product);
    }

    protected MenuGroup MenuGroup을_저장한다(String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        menuGroupRepository.save(menuGroup);
        return menuGroup;
    }

    protected Menu Menu를_저장한다(String name, int price, MenuGroup menuGroup) {
        final Menu menu = new Menu(name, price, menuGroup);
        return menuRepository.save(menu);
    }

    protected Order Order를_저장한다(OrderTable orderTable, OrderStatus orderStatus) {
        return orderRepository.save(new Order(orderTable, orderStatus));
    }

    protected OrderTable OrderTable을_저장한다(TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
        final OrderTable orderTable = new OrderTable(tableGroup, numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }

    protected TableGroup TableGroup을_저장한다() {
        return tableGroupRepository.save(new TableGroup());
    }

    protected void POST_API를_요청하면_BadRequest를_응답한다(String apiPath, Object reqrequestBody) throws Exception {
        mockMvc.perform(post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(reqrequestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE));
    }

    protected void PUT_API를_요청하면_BadRequest를_응답한다(String apiPath, Object requestBody) throws Exception {
        mockMvc.perform(put(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
        ;
    }

    protected void Repository가_비어있다(JpaRepository<?, Long> repository) {
        final List<?> foundAll = repository.findAll();
        assertThat(foundAll).isEmpty();
    }
}
