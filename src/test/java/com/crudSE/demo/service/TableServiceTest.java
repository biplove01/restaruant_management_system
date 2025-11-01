package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Table;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TableService tableService;

    private Table table;

    @BeforeEach
    void setUp() {
        table = new Table();
        table.setId(1L);
        table.setTableNumber("T1");
        table.setOrderList(new ArrayList<>());
    }

    @Test
    void createTable_Success() {
        when(tableRepository.existsByTableNumber("T1")).thenReturn(false);
        when(tableRepository.save(any(Table.class))).thenReturn(table);

        Table result = tableService.createTable(table);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("T1", result.getTableNumber());
        verify(tableRepository, times(1)).existsByTableNumber("T1");
        verify(tableRepository, times(1)).save(any(Table.class));
    }

    @Test
    void createTable_AlreadyExistsException() {
        when(tableRepository.existsByTableNumber("T1")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> {
            tableService.createTable(table);
        });

        verify(tableRepository, times(1)).existsByTableNumber("T1");
        verify(tableRepository, never()).save(any(Table.class));
    }

    @Test
    void getTableById_Success() {
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));

        Table result = tableService.getTableById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("T1", result.getTableNumber());
        verify(tableRepository, times(1)).findById(1L);
    }

    @Test
    void getTableById_NotFound() {
        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tableService.getTableById(999L);
        });

        verify(tableRepository, times(1)).findById(999L);
    }

    @Test
    void getAllTables_Success() {
        Table table2 = new Table();
        table2.setId(2L);
        table2.setTableNumber("T2");
        table2.setOrderList(new ArrayList<>());

        List<Table> tables = Arrays.asList(table, table2);
        when(tableRepository.findAll()).thenReturn(tables);

        List<Table> result = tableService.getAllTables();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).getTableNumber());
        assertEquals("T2", result.get(1).getTableNumber());
        verify(tableRepository, times(1)).findAll();
    }

    @Test
    void getAllTables_EmptyList() {
        when(tableRepository.findAll()).thenReturn(new ArrayList<>());

        List<Table> result = tableService.getAllTables();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tableRepository, times(1)).findAll();
    }

    @Test
    void deleteTable_Success() {
        when(tableRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tableRepository).deleteById(1L);

        String result = tableService.deleteTable(1L);

        assertEquals("Table with id 1 successfully deleted", result);
        verify(tableRepository, times(1)).existsById(1L);
        verify(tableRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTable_NotFound() {
        when(tableRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            tableService.deleteTable(999L);
        });

        verify(tableRepository, times(1)).existsById(999L);
        verify(tableRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateTable_Success() {
        Table updatedTable = new Table();
        updatedTable.setTableNumber("T1-UPDATED");

        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(tableRepository.save(any(Table.class))).thenReturn(table);

        Table result = tableService.updateTable(1L, updatedTable);

        assertNotNull(result);
        assertEquals("T1-UPDATED", table.getTableNumber());
        verify(tableRepository, times(1)).findById(1L);
        verify(tableRepository, times(1)).save(any(Table.class));
    }

    @Test
    void updateTable_NotFound() {
        Table updatedTable = new Table();
        updatedTable.setTableNumber("T1-UPDATED");

        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tableService.updateTable(999L, updatedTable);
        });

        verify(tableRepository, times(1)).findById(999L);
        verify(tableRepository, never()).save(any(Table.class));
    }
}
