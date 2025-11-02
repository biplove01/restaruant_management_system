package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.enums.MenuItemCategory;
import com.crudSE.demo.repositories.MenuItemRepository;
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
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

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
    void createMenuItem_Success() {
        when(menuItemRepository.existsByName(menuItem.getName())).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = menuItemService.createMenuItem(menuItem);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Burger", result.getName());
        assertEquals(12.99f, result.getPrice());
        assertEquals(MenuItemCategory.LUNCH, result.getCategory());
        verify(menuItemRepository, times(1)).existsByName(menuItem.getName());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void createMenuItem_AlreadyExistsException() {
        when(menuItemRepository.existsByName(menuItem.getName())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> {
            menuItemService.createMenuItem(menuItem);
        });

        verify(menuItemRepository, times(1)).existsByName(menuItem.getName());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void getMenuItemById_Success() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItem result = menuItemService.getMenuItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Burger", result.getName());
        verify(menuItemRepository, times(1)).findById(1L);
    }

    @Test
    void getMenuItemById_NotFound() {
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.getMenuItemById(999L);
        });

        verify(menuItemRepository, times(1)).findById(999L);
    }

    @Test
    void getMenuItemByName_Success() {
        when(menuItemRepository.findByName("Burger")).thenReturn(Optional.of(menuItem));

        MenuItem result = menuItemService.getMenuItemByName("Burger");

        assertNotNull(result);
        assertEquals("Burger", result.getName());
        verify(menuItemRepository, times(1)).findByName("Burger");
    }

    @Test
    void getMenuItemByName_NotFound() {
        when(menuItemRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.getMenuItemByName("NonExistent");
        });

        verify(menuItemRepository, times(1)).findByName("NonExistent");
    }

    @Test
    void getAllMenuItems_Success() {
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setName("Pizza");
        menuItem2.setPrice(15.99f);
        menuItem2.setCategory(MenuItemCategory.LUNCH);

        List<MenuItem> menuItems = Arrays.asList(menuItem, menuItem2);
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        List<MenuItem> result = menuItemService.getAllMenuItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    void getAllMenuItems_EmptyList() {
        when(menuItemRepository.findAll()).thenReturn(new ArrayList<>());

        List<MenuItem> result = menuItemService.getAllMenuItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    void updateMenuItem_Success() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setName("Burger");
        updatedMenuItem.setPrice(14.99f);
        updatedMenuItem.setCategory(MenuItemCategory.LUNCH);

        when(menuItemRepository.existsByName("Burger")).thenReturn(true);
        when(menuItemRepository.existsById(1L)).thenReturn(true);
        when(menuItemRepository.findByName("Burger")).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = menuItemService.updateMenuItem(updatedMenuItem);

        assertNotNull(result);
        verify(menuItemRepository, times(1)).existsByName("Burger");
        verify(menuItemRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).findByName("Burger");
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_NotFound_ByExistence() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(999L);
        updatedMenuItem.setName("NonExistent");

        when(menuItemRepository.existsByName("NonExistent")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.updateMenuItem(updatedMenuItem);
        });

        verify(menuItemRepository, times(1)).existsByName("NonExistent");
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_NotFound_ByName() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setName("Burger");

        when(menuItemRepository.existsByName("Burger")).thenReturn(true);
        when(menuItemRepository.existsById(1L)).thenReturn(true);
        when(menuItemRepository.findByName("Burger")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.updateMenuItem(updatedMenuItem);
        });

        verify(menuItemRepository, times(1)).existsByName("Burger");
        verify(menuItemRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).findByName("Burger");
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_Success() {
        when(menuItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(menuItemRepository).deleteById(1L);

        String result = menuItemService.deleteMenuItem(1L);

        assertEquals("Menu item successfully deleted", result);
        verify(menuItemRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMenuItem_NotFound() {
        when(menuItemRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.deleteMenuItem(999L);
        });

        verify(menuItemRepository, times(1)).existsById(999L);
        verify(menuItemRepository, never()).deleteById(anyLong());
    }
}
