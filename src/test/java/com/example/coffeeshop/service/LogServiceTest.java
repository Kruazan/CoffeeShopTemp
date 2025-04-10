package com.example.coffeeshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    private final LogService logService = new LogService();

    @Test
    void testGetLogFile_LogNotFound_ShouldThrowException() {
        assertThrows(FileNotFoundException.class, () -> {
            logService.getLogFile("2025-05-09");
        });
    }

    @Test
    void testGetLogFile_LogFound_ShouldReturnFile() throws Exception {
        File result = logService.getLogFile(null);
        assertNotNull(result);
        assertTrue(result.exists());
    }
}
