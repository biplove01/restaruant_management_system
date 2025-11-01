package com.crudSE.demo.controller;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.OrderItem;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.models.enums.MenuItemCategory;
import com.crudSE.demo.models.enums.OrderStatus;
import com.crudSE.demo.service.OrderListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderListController.class)
class OrderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderListService orderListService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderList orderList;
    private Table table;
    private MenuItem menuItem;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        table = new Table();
        table.setId(1L);
        table.setTableNumber("T1");

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(12.99f);
        menuItem.setCategory(MenuItemCategory.LUNCH);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setOrderStatus(OrderStatus.PENDING);
        orderItem.setMenuItem(menuItem);

        orderList = new OrderList();
        orderList.setId(1L);
        orderList.setTable(table);
        orderList.setOrderItems(Arrays.asList(orderItem));
    }

    @Test
    void createOrderList_Success() throws Exception {
        when(orderListService.createOrderList(any(OrderList.class))).thenReturn(orderList);

        mockMvc.perform(post("/api/orderList/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderItems.length()").value(1));

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void createOrderList_TableNotFound() throws Exception {
        when(orderListService.createOrderList(any(OrderList.class)))
                .thenThrow(new ResourceNotFoundException("Table with ID: 999 does not exist"));

        mockMvc.perform(post("/api/orderList/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderList)))
                .andExpect(status().isNotFound());

        verify(orderListService, times(1)).createOrderList(any(OrderList.class));
    }

    @Test
    void getOrderListById_Success() throws Exception {
        orderList.setId(7L);
        when(orderListService.getOrderListById(7L)).thenReturn(orderList);

        mockMvc.perform(get("/api/orderList/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L));

        verify(orderListService, times(1)).getOrderListById(7L);
    }

    @Test
    void getOrderListById_NotFound() throws Exception {
        when(orderListService.getOrderListById(999L))
                .thenThrow(new ResourceNotFoundException("Order list of id: 999 does not exist"));

        mockMvc.perform(get("/api/orderList/999"))
                .andExpect(status().isNotFound());

        verify(orderListService, times(1)).getOrderListById(999L);
    }

    @Test
    void getAllOrderLists_Success() throws Exception {
        OrderList orderList2 = new OrderList();
        orderList2.setId(2L);
        orderList2.setTable(table);
        orderList2.setOrderItems(new ArrayList<>());

        List<OrderList> orderLists = Arrays.asList(orderList, orderList2);
        when(orderListService.getAllOrderLists()).thenReturn(orderLists);

        mockMvc.perform(get("/api/orderList/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(orderListService, times(1)).getAllOrderLists();
    }

    @Test
    void getAllOrderLists_EmptyList() throws Exception {
        when(orderListService.getAllOrderLists()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/orderList/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orderListService, times(1)).getAllOrderLists();
    }

    @Test
    void updateOrderList_Success() throws Exception {
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setQuantity(3);
        newOrderItem.setOrderStatus(OrderStatus.READY);
        newOrderItem.setMenuItem(menuItem);
        orderList.setOrderItems(Arrays.asList(newOrderItem));

        when(orderListService.updateOrderList(any(OrderList.class))).thenReturn(orderList);

        mockMvc.perform(post("/api/orderList/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderItems.length()").value(1));

        verify(orderListService, times(1)).updateOrderList(any(OrderList.class));
    }

    @Test
    void updateOrderList_NotFound() throws Exception {
        orderList.setId(999L);
        when(orderListService.updateOrderList(any(OrderList.class)))
                .thenThrow(new ResourceNotFoundException("Order list of id: 999 is not found!"));

        mockMvc.perform(post("/api/orderList/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderList)))
                .andExpect(status().isNotFound());

        verify(orderListService, times(1)).updateOrderList(any(OrderList.class));
    }

    @Test
    void deleteOrderList_Success() throws Exception {
        when(orderListService.deleteOrderList(1L)).thenReturn("Order list of id: 1 successfully deleted");

        mockMvc.perform(delete("/api/orderList/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order list of id: 1 successfully deleted"));

        verify(orderListService, times(1)).deleteOrderList(1L);
    }

    @Test
    void deleteOrderList_NotFound() throws Exception {
        when(orderListService.deleteOrderList(999L))
                .thenThrow(new ResourceNotFoundException("Order list of id: 999 does not exist"));

        mockMvc.perform(delete("/api/orderList/999"))
                .andExpect(status().isNotFound());

        verify(orderListService, times(1)).deleteOrderList(999L);
    }
}
