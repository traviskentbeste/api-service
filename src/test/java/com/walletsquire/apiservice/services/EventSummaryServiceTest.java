package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.EventSummary;
import com.walletsquire.apiservice.repositories.EventSummaryRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("dev")
public class EventSummaryServiceTest {

    @Mock
    private EventSummaryRepository repository;

    @InjectMocks
    private EventSummaryService service;

    @Test
    @Order(1)
    void save() {

        // Arrange
        final var obj = EventSummary.builder().build();
        when(repository.save(any(EventSummary.class))).thenReturn(obj);

        // Act
        final var actual = service.create(new EventSummary());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(obj);
        verify(repository, times(1)).save(any(EventSummary.class));
        verifyNoMoreInteractions(repository);

    }

    @Test
    @Order(2)
    void findById() {

        // Arrange
        final var obj = Optional.of(EventSummary.builder().build());
        when(repository.findById(anyLong())).thenReturn((Optional<EventSummary>) obj);

        // Act
        final var actual = service.getById(getRandomLong());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(obj);
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);

    }


    @Test
    @Order(3)
    void findAll() {

        // Arrange
        when(repository.findAll()).thenReturn(List.of(new EventSummary(), new EventSummary()));

        // Act & Assert
        assertThat(service.getAll()).hasSize(2);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);

    }

    @Test
    @Order(4)
    void update() {

        // Arrange
        final var obj = EventSummary.builder().id(1L).build();
        final var updated = EventSummary.builder().id(1L).build();
        // update does a find by id
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(updated));

        // Act & Assert
        service.update(obj, 1L);
        verify(repository, times(1)).save(updated);
        verifyNoMoreInteractions(repository);

    }

    @Test
    @Order(5)
    void delete() {

        // Arrange
        final var obj = EventSummary.builder().id(1L).build();
        doNothing().when(repository).delete(obj);
        // delete does a find by id
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(obj));

        // Act & Assert
        service.delete(obj);
        verify(repository, times(1)).delete(obj);
        verifyNoMoreInteractions(repository);

    }

    private Long getRandomLong() {

        int randomInt = new Random().ints(1, 10).findFirst().getAsInt();

        return Long.valueOf(randomInt);

    }

}
