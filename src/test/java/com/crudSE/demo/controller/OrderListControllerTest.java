package com.crudSE.demo.controller;

import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.enums.OrderStatus;
import com.crudSE.demo.service.OrderListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderListControllerTest {

    @Mock
    private OrderListService orderListService;

    @InjectMocks
    private OrderListController orderListController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private OrderList testOrderList;
    private OrderList testOrderList2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderListController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testOrderList = new OrderList();
        testOrderList.setId(1L);
        testOrderList.setOrderDate(LocalDateTime.now());
        testOrderList.setTotalAmount(45.99f);
        testOrderList.setStatus(OrderStatus.PENDING);

        testOrderList2 = new OrderList();
        testOrderList2.setId(2L);
        testOrderList2.setOrderDate(LocalDateTime.now().minusHours(1));
        testOrderList2.setTotalAmount(32.50f);
        testOrderList2.setStatus(OrderStatus.COMPLETED);
    }

    @Test
    void createOrderList_Success() throws Exception {
        // Given
        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(testOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(45.99))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void createOrderList_WithInvalidData_ReturnsOk() throws Exception {
        // Given
        OrderList invalidOrderList = new OrderList();
        invalidOrderList.setTotalAmount(-10.0f); // Invalid amount

        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(invalidOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidOrderList)))
                .andExpect(status().isOk());

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void getOrderListById_Success() throws Exception {
        // Given
        when(orderListService.getOrderListById(1L)).thenReturn(testOrderList);

        // When & Then
        mockMvc.perform(get("/api/orderList/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(45.99))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderListService, times(1)).getOrderListById(1L);
    }

    @Test
    void getOrderListById_NotFound() throws Exception {
        // Given
        when(orderListService.getOrderListById(999L)).thenThrow(new RuntimeException("OrderList not found"));

        // When & Then
        mockMvc.perform(get("/api/orderList/999"))
                .andExpect(status().isInternalServerError());

        verify(orderListService, times(1)).getOrderListById(999L);
    }

    @Test
    void getAllOrderLists_Success() throws Exception {
        // Given
        List<OrderList> orderLists = Arrays.asList(testOrderList, testOrderList2);
        when(orderListService.getAllOrderLists()).thenReturn(orderLists);

        // When & Then
        mockMvc.perform(get("/api/orderList/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalAmount").value(45.99))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].totalAmount").value(32.50))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));

        verify(orderListService, times(1)).getAllOrderLists();
    }

    @Test
    void getAllOrderLists_EmptyList() throws Exception {
        // Given
        when(orderListService.getAllOrderLists()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/orderList/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(orderListService, times(1)).getAllOrderLists();
    }

    @Test
    void updateOrderList_Success() throws Exception {
        // Given
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(1L);
        updatedOrderList.setTotalAmount(50.99f);
        updatedOrderList.setStatus(OrderStatus.IN_PROGRESS);

        when(orderListService.updateOrderList(any(OrderList.class))).thenReturn(updatedOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrderList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(50.99))
                .andExpect(jsonPath("$.status").value(OrderStatus.IN_PROGRESS));

        verify(orderListService, times(1)).updateOrderList(any(OrderList.class));
    }

    @Test
    void updateOrderList_WithStatusChange() throws Exception {
        // Given
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(1L);
        updatedOrderList.setTotalAmount(45.99f);
        updatedOrderList.setStatus(OrderStatus.COMPLETED); // Status changed

        when(orderListService.updateOrderList(any(OrderList.class))).thenReturn(updatedOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrderList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.COMPLETED));

        verify(orderListService, times(1)).updateOrderList(any(OrderList.class));
    }

    @Test
    void deleteOrderList_Success() throws Exception {
        // Given
        when(orderListService.deleteOrderList(any(OrderList.class))).thenReturn("OrderList deleted successfully");

        // When & Then
        mockMvc.perform(delete("/api/orderList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderList)))
                .andExpect(status().isOk())
                .andExpect(content().string("OrderList deleted successfully"));

        verify(orderListService, times(1)).deleteOrderList(any(OrderList.class));
    }

    @Test
    void deleteOrderList_NotFound() throws Exception {
        // Given
        when(orderListService.deleteOrderList(any(OrderList.class))).thenReturn("OrderList not found");

        // When & Then
        mockMvc.perform(delete("/api/orderList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderList)))
                .andExpect(status().isOk())
                .andExpect(content().string("OrderList not found"));

        verify(orderListService, times(1)).deleteOrderList(any(OrderList.class));
    }

    @Test
    void controller_HandlesNullValues() throws Exception {
        // Given
        OrderList nullOrderList = new OrderList();
        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(testOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullOrderList)))
                .andExpect(status().isOk());

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void controller_HandlesEmptyRequestBody() throws Exception {
        // Given
        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(testOrderList);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void controller_HandlesDifferentOrderStatuses() throws Exception {
        // Given
        OrderList cancelledOrder = new OrderList();
        cancelledOrder.setId(3L);
        cancelledOrder.setTotalAmount(25.00f);
        cancelledOrder.setStatus(OrderStatus.CANCELLED);

        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(cancelledOrder);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelledOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED));

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void controller_HandlesZeroAmount() throws Exception {
        // Given
        OrderList zeroAmountOrder = new OrderList();
        zeroAmountOrder.setId(4L);
        zeroAmountOrder.setTotalAmount(0.0f);
        zeroAmountOrder.setStatus(OrderStatus.PENDING);

        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(zeroAmountOrder);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zeroAmountOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(0.0));

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void controller_HandlesLargeAmounts() throws Exception {
        // Given
        OrderList largeAmountOrder = new OrderList();
        largeAmountOrder.setId(5L);
        largeAmountOrder.setTotalAmount(999.99f);
        largeAmountOrder.setStatus(OrderStatus.PENDING);

        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(largeAmountOrder);

        // When & Then
        mockMvc.perform(post("/api/orderList/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(largeAmountOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(999.99));

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }
}
