package com.crudSE.demo.controller;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.enums.MenuItemCategory;
import com.crudSE.demo.service.MenuItemService;
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

@WebMvcTest(MenuItemController.class)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(12.99f);
        menuItem.setCategory(MenuItemCategory.LUNCH);
    }

    @Test
    void createMenuItem_Success() throws Exception {
        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(menuItem);

        mockMvc.perform(post("/api/menuItem/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(12.99))
                .andExpect(jsonPath("$.category").value("LUNCH"));

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void createMenuItem_AlreadyExistsException() throws Exception {
        when(menuItemService.createMenuItem(any(MenuItem.class)))
                .thenThrow(new AlreadyExistsException("Menu item with the name: Burger already exists!"));

        mockMvc.perform(post("/api/menuItem/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isBadRequest());

        verify(menuItemService, times(1)).createMenuItem(any(MenuItem.class));
    }

    @Test
    void getMenuItemById_Success() throws Exception {
        when(menuItemService.getMenuItemById(1L)).thenReturn(menuItem);

        mockMvc.perform(get("/api/menuItem/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(12.99));

        verify(menuItemService, times(1)).getMenuItemById(1L);
    }

    @Test
    void getMenuItemById_NotFound() throws Exception {
        when(menuItemService.getMenuItemById(999L))
                .thenThrow(new ResourceNotFoundException("Menu item with id: 999 does not exists"));

        mockMvc.perform(get("/api/menuItem/999"))
                .andExpect(status().isNotFound());

        verify(menuItemService, times(1)).getMenuItemById(999L);
    }

    @Test
    void getAllMenuItems_Success() throws Exception {
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setName("Pizza");
        menuItem2.setPrice(15.99f);
        menuItem2.setCategory(MenuItemCategory.LUNCH);

        List<MenuItem> menuItems = Arrays.asList(menuItem, menuItem2);
        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        mockMvc.perform(get("/api/menuItem/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Burger"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Pizza"));

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    void getAllMenuItems_EmptyList() throws Exception {
        when(menuItemService.getAllMenuItems()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/menuItem/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    void updateMenuItem_Success() throws Exception {
        menuItem.setPrice(14.99f);
        when(menuItemService.updateMenuItem(any(MenuItem.class))).thenReturn(menuItem);

        mockMvc.perform(post("/api/menuItem/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(14.99));

        verify(menuItemService, times(1)).updateMenuItem(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_NotFound() throws Exception {
        menuItem.setId(999L);
        when(menuItemService.updateMenuItem(any(MenuItem.class)))
                .thenThrow(new ResourceNotFoundException("No such menu item exists with name: Burger and id: 999"));

        mockMvc.perform(post("/api/menuItem/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isNotFound());

        verify(menuItemService, times(1)).updateMenuItem(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_Success() throws Exception {
        when(menuItemService.deleteMenuItem(1L)).thenReturn("Menu item successfully deleted");

        mockMvc.perform(delete("/api/menuItem/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Menu item successfully deleted"));

        verify(menuItemService, times(1)).deleteMenuItem(1L);
    }

    @Test
    void deleteMenuItem_NotFound() throws Exception {
        when(menuItemService.deleteMenuItem(999L))
                .thenThrow(new ResourceNotFoundException("No such menu item exists with id: 999"));

        mockMvc.perform(delete("/api/menuItem/999"))
                .andExpect(status().isNotFound());

        verify(menuItemService, times(1)).deleteMenuItem(999L);
    }
}
