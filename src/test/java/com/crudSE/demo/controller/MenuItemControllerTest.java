package com.crudSE.demo.controller;

import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.enums.MenuItemCategory;
import com.crudSE.demo.service.MenuItemService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private MenuItem testMenuItem;
    private MenuItem testMenuItem2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setName("Margherita Pizza");
        testMenuItem.setPrice(15.99f);
        testMenuItem.setCategory(MenuItemCategory.LUNCH);

        testMenuItem2 = new MenuItem();
        testMenuItem2.setId(2L);
        testMenuItem2.setName("Cappuccino");
        testMenuItem2.setPrice(4.50f);
        testMenuItem2.setCategory(MenuItemCategory.HOT_DRINK);
    }

    @Test
    void createMenuItem_Success() throws Exception {
        // Given
        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(testMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Margherita Pizza"))
                .andExpect(jsonPath("$.price").value(15.99))
                .andExpect(jsonPath("$.category").value("LUNCH"));

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void createMenuItem_WithInvalidData_ReturnsOk() throws Exception {
        // Given
        MenuItem invalidMenuItem = new MenuItem();
        invalidMenuItem.setName(""); // Invalid name
        invalidMenuItem.setPrice(-5.0f); // Invalid price

        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(invalidMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMenuItem)))
                .andExpect(status().isOk());

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void getMenuItemById_Success() throws Exception {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(testMenuItem);

        // When & Then
        mockMvc.perform(get("/api/menuItem/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Margherita Pizza"))
                .andExpect(jsonPath("$.price").value(15.99))
                .andExpect(jsonPath("$.category").value("LUNCH"));

        verify(menuItemService, times(1)).getMenuItemById(1L);
    }

    @Test
    void getMenuItemById_NotFound() throws Exception {
        // Given
        when(menuItemService.getMenuItemById(999L)).thenThrow(new RuntimeException("MenuItem not found"));

        // When & Then
        mockMvc.perform(get("/api/menuItem/999"))
                .andExpect(status().isInternalServerError());

        verify(menuItemService, times(1)).getMenuItemById(999L);
    }

    @Test
    void getAllMenuItems_Success() throws Exception {
        // Given
        List<MenuItem> menuItems = Arrays.asList(testMenuItem, testMenuItem2);
        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        // When & Then
        mockMvc.perform(get("/api/menuItem/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Margherita Pizza"))
                .andExpect(jsonPath("$[0].category").value("LUNCH"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Cappuccino"))
                .andExpect(jsonPath("$[1].category").value("HOT_DRINK"));

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    void getAllMenuItems_EmptyList() throws Exception {
        // Given
        when(menuItemService.getAllMenuItems()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/menuItem/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    void updateMenuItem_Success() throws Exception {
        // Given
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setName("Margherita Pizza Updated");
        updatedMenuItem.setPrice(18.99f);
        updatedMenuItem.setCategory(MenuItemCategory.LUNCH);

        when(menuItemService.updateMenuItem(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Margherita Pizza Updated"))
                .andExpect(jsonPath("$.price").value(18.99))
                .andExpect(jsonPath("$.category").value("LUNCH"));

        verify(menuItemService, times(1)).updateMenuItem(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_WithDifferentCategory() throws Exception {
        // Given
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setName("Margherita Pizza");
        updatedMenuItem.setPrice(15.99f);
        updatedMenuItem.setCategory(MenuItemCategory.BREAKFAST); // Changed category

        when(menuItemService.updateMenuItem(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("BREAKFAST"));

        verify(menuItemService, times(1)).updateMenuItem(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_Success() throws Exception {
        // Given
        when(menuItemService.deleteMenuItem(any(MenuItem.class))).thenReturn("MenuItem deleted successfully");

        // When & Then
        mockMvc.perform(delete("/api/menuItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMenuItem)))
                .andExpect(status().isOk())
                .andExpect(content().string("MenuItem deleted successfully"));

        verify(menuItemService, times(1)).deleteMenuItem(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_NotFound() throws Exception {
        // Given
        when(menuItemService.deleteMenuItem(any(MenuItem.class))).thenReturn("MenuItem not found");

        // When & Then
        mockMvc.perform(delete("/api/menuItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMenuItem)))
                .andExpect(status().isOk())
                .andExpect(content().string("MenuItem not found"));

        verify(menuItemService, times(1)).deleteMenuItem(any(MenuItem.class));
    }

    @Test
    void controller_HandlesNullValues() throws Exception {
        // Given
        MenuItem nullMenuItem = new MenuItem();
        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(testMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullMenuItem)))
                .andExpect(status().isOk());

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void controller_HandlesEmptyRequestBody() throws Exception {
        // Given
        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(testMenuItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void controller_HandlesAllMenuItemCategories() throws Exception {
        // Given
        MenuItem breakfastItem = new MenuItem();
        breakfastItem.setId(3L);
        breakfastItem.setName("Pancakes");
        breakfastItem.setPrice(12.99f);
        breakfastItem.setCategory(MenuItemCategory.BREAKFAST);

        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(breakfastItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(breakfastItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("BREAKFAST"));

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void controller_HandlesPricePrecision() throws Exception {
        // Given
        MenuItem precisePriceItem = new MenuItem();
        precisePriceItem.setId(4L);
        precisePriceItem.setName("Special Coffee");
        precisePriceItem.setPrice(4.99f);
        precisePriceItem.setCategory(MenuItemCategory.HOT_DRINK);

        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(precisePriceItem);

        // When & Then
        mockMvc.perform(post("/api/menuItem/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(precisePriceItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(4.99));

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }
}
