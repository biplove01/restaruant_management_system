package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.OrderItem;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.models.enums.MenuItemCategory;
import com.crudSE.demo.models.enums.OrderStatus;
import com.crudSE.demo.repositories.OrderListRepository;
import com.crudSE.demo.repositories.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderListServiceTest {

    @Mock
    private OrderListRepository orderListRepository;

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private OrderListService orderListService;

    private OrderList orderList;
    private Table table;
    private MenuItem menuItem;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        table = new Table();
        table.setId(1L);
        table.setTableNumber("T1");
        table.setOrderList(new ArrayList<>());

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
        orderList.setOrderItems(new ArrayList<>(Arrays.asList(orderItem)));
    }

    @Test
    void createOrderList_Success() {
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(orderListRepository.save(any(OrderList.class))).thenReturn(orderList);

        OrderList result = orderListService.createOrderList(orderList);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tableRepository, times(1)).findById(1L);
        verify(orderListRepository, times(1)).save(any(OrderList.class));
    }

    @Test
    void createOrderList_TableNotFound() {
        orderList.getTable().setId(999L);
        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderListService.createOrderList(orderList);
        });

        verify(tableRepository, times(1)).findById(999L);
        verify(orderListRepository, never()).save(any(OrderList.class));
    }

    @Test
    void getOrderListById_Success() {
        when(orderListRepository.findById(1L)).thenReturn(Optional.of(orderList));

        OrderList result = orderListService.getOrderListById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderListRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderListById_NotFound() {
        when(orderListRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderListService.getOrderListById(999L);
        });

        verify(orderListRepository, times(1)).findById(999L);
    }

    @Test
    void getAllOrderLists_Success() {
        OrderList orderList2 = new OrderList();
        orderList2.setId(2L);
        orderList2.setTable(table);
        orderList2.setOrderItems(new ArrayList<>());

        List<OrderList> orderLists = Arrays.asList(orderList, orderList2);
        when(orderListRepository.findAll()).thenReturn(orderLists);

        List<OrderList> result = orderListService.getAllOrderLists();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(orderListRepository, times(1)).findAll();
    }

    @Test
    void getAllOrderLists_EmptyList() {
        when(orderListRepository.findAll()).thenReturn(new ArrayList<>());

        List<OrderList> result = orderListService.getAllOrderLists();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderListRepository, times(1)).findAll();
    }

    @Test
    void updateOrderList_Success() {
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(1L);
        updatedOrderList.setTable(table);

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setQuantity(3);
        newOrderItem.setOrderStatus(OrderStatus.READY);
        newOrderItem.setMenuItem(menuItem);
        updatedOrderList.setOrderItems(new ArrayList<>(Arrays.asList(newOrderItem)));

        when(orderListRepository.findById(1L)).thenReturn(Optional.of(orderList));
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(orderListRepository.save(any(OrderList.class))).thenReturn(orderList);

        OrderList result = orderListService.updateOrderList(updatedOrderList);

        assertNotNull(result);
        verify(orderListRepository, times(1)).findById(1L);
        verify(tableRepository, times(1)).findById(1L);
        verify(orderListRepository, times(1)).save(any(OrderList.class));
    }

    @Test
    void updateOrderList_OrderListNotFound() {
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(999L);

        when(orderListRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderListService.updateOrderList(updatedOrderList);
        });

        verify(orderListRepository, times(1)).findById(999L);
        verify(orderListRepository, never()).save(any(OrderList.class));
    }

    @Test
    void updateOrderList_TableNotFound() {
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(1L);
        Table newTable = new Table();
        newTable.setId(999L);
        updatedOrderList.setTable(newTable);
        updatedOrderList.setOrderItems(new ArrayList<>());

        when(orderListRepository.findById(1L)).thenReturn(Optional.of(orderList));
        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderListService.updateOrderList(updatedOrderList);
        });

        verify(orderListRepository, times(1)).findById(1L);
        verify(tableRepository, times(1)).findById(999L);
        verify(orderListRepository, never()).save(any(OrderList.class));
    }

    @Test
    void updateOrderList_WithNullTable() {
        OrderList updatedOrderList = new OrderList();
        updatedOrderList.setId(1L);
        updatedOrderList.setTable(null);
        updatedOrderList.setOrderItems(new ArrayList<>());

        when(orderListRepository.findById(1L)).thenReturn(Optional.of(orderList));
        when(orderListRepository.save(any(OrderList.class))).thenReturn(orderList);

        OrderList result = orderListService.updateOrderList(updatedOrderList);

        assertNotNull(result);
        verify(orderListRepository, times(1)).findById(1L);
        verify(tableRepository, never()).findById(anyLong());
        verify(orderListRepository, times(1)).save(any(OrderList.class));
    }

    @Test
    void deleteOrderList_Success() {
        when(orderListRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderListRepository).deleteById(1L);

        String result = orderListService.deleteOrderList(1L);

        assertEquals("Order list of id: 1 successfully deleted", result);
        verify(orderListRepository, times(1)).existsById(1L);
        verify(orderListRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrderList_NotFound() {
        when(orderListRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            orderListService.deleteOrderList(999L);
        });

        verify(orderListRepository, times(1)).existsById(999L);
        verify(orderListRepository, never()).deleteById(anyLong());
    }
}
