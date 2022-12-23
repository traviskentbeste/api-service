package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.repositories.PaidUsersRepository;
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

import java.math.BigDecimal;
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
public class PaidUsersServiceTest {

    @Mock
    private PaidUsersRepository repository;

    @InjectMocks
    private PaidUsersService service;

    @Test
    @Order(1)
    void save() {

        // Arrange
        final var obj = PaidUsers.builder().amount(BigDecimal.valueOf(1)).build();
        when(repository.save(any(PaidUsers.class))).thenReturn(obj);

        // Act
        final var actual = service.create(new PaidUsers());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(obj);
        verify(repository, times(1)).save(any(PaidUsers.class));
        verifyNoMoreInteractions(repository);

    }

    @Test
    @Order(2)
    void findById() {

        // Arrange
        final var obj = Optional.of(PaidUsers.builder().amount(BigDecimal.valueOf(1)).build());
        when(repository.findById(anyLong())).thenReturn((Optional<PaidUsers>) obj);

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
        when(repository.findAll()).thenReturn(List.of(new PaidUsers(), new PaidUsers()));

        // Act & Assert
        assertThat(service.getAll()).hasSize(2);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);

    }

    @Test
    @Order(4)
    void update() {

        // Arrange
        final var obj = PaidUsers.builder().id(1L).amount(BigDecimal.valueOf(1)).build();
        final var updated = PaidUsers.builder().id(1L).amount(BigDecimal.valueOf(2)).build();
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
        final var obj = PaidUsers.builder().id(1L).amount(BigDecimal.valueOf(1)).build();
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
