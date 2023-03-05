package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.*;
import com.walletsquire.apiservice.entities.*;
import com.walletsquire.apiservice.mappers.ActivityMapper;
import com.walletsquire.apiservice.services.*;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest( controllers = ActivityController.class )
@ComponentScan(basePackageClasses = ActivityMapper.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("dev")
@TestMethodOrder(OrderAnnotation.class)
public class ActivityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ActivityMapper mapper;

    @Autowired
    ActivityController controller;

    String endpoint = "/api/v1" + controller.endpoint;

    // mock objects
    @MockBean
    ActivityService service;

    @MockBean
    UserService userService;

    @MockBean
    AddressService addressService;

    @MockBean
    CurrencyService currencyService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    EventService eventService;

    @MockBean
    PaidService paidService;

    @MockBean
    CreditorService creditorService;

    @MockBean
    DebitorService debitorService;

    @MockBean
    EventSummaryService eventSummaryService;

    /* entities/DTOs go here for testing */
    Activity entity1 = new Activity();
    Activity entity2 = new Activity();
    ActivityDTO entityDTO1 = new ActivityDTO();
    ActivityDTO entityDTO2 = new ActivityDTO();

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    public void create() throws Exception {

        Event event = new Event();
        event.setId(1L);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);

        Currency currency = new Currency();
        currency.setId(1L);

        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1L);

        Address address = new Address();
        address.setId(1L);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);

        Category category = new Category();
        category.setId(1L);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);

        Paid paidBy = new Paid();
        paidBy.setId(1L);

        PaidDTO paidByDTO = new PaidDTO();
        paidByDTO.setId(1L);

        Paid paidFor = new Paid();
        paidFor.setId(1L);

        PaidDTO paidForDTO = new PaidDTO();
        paidForDTO.setId(1L);

        entity1.setId(1L);
        entity1.setName("myName1");
        entity1.setEvent(event);
        entity1.setCurrency(currency);
        entity1.setAddress(address);
        entity1.setCategory(category);
        entity1.setPaidBy(paidBy);
        entity1.setPaidFor(paidFor);

        entityDTO1.setId(1L);
        entityDTO1.setName("myName1");
        entityDTO1.setEvent(eventDTO);
        entityDTO1.setCurrency(currencyDTO);
        entityDTO1.setAddress(addressDTO);
        entityDTO1.setCategory(categoryDTO);
        entityDTO1.setPaidBy(paidByDTO);
        entityDTO1.setPaidFor(paidForDTO);

        when(eventService.getById(any(Long.class))).thenReturn(Optional.of(event));
        when(currencyService.getById(any(Long.class))).thenReturn(Optional.of(currency));
        when(addressService.getById(any(Long.class))).thenReturn(Optional.of(address));
        when(categoryService.getById(any(Long.class))).thenReturn(Optional.of(category));
        when(paidService.getById(any(Long.class))).thenReturn(Optional.of(paidBy));
        when(paidService.getById(any(Long.class))).thenReturn(Optional.of(paidFor));

        when(service.create(any(Activity.class))).thenReturn(entity1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(entityDTO1))
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("myName1")))
                .andDo(print())
                ;

    }

    @Test
    @Order(2)
    public void getById() throws Exception {

        entity1.setId(1L);
        entity1.setName("myName1");

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint + "/" + entity1.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("myName1")));

    }

    @Test
    @Order(3)
    public void getAll() throws Exception {

        entity1.setName("myName1");

        List<Activity> entitys = new ArrayList<>(Arrays.asList(entity1, entity2));

        when(service.getAll()).thenReturn(entitys);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("myName1")));

    }

    @Test
    @Order(4)
    public void update() throws Exception {

        Event event = new Event();
        event.setId(1L);
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);

        Currency currency = new Currency();
        currency.setId(1L);
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1L);

        Address address = new Address();
        address.setId(1L);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);

        Category category = new Category();
        category.setId(1L);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);

        Paid paidBy = new Paid();
        paidBy.setId(1L);

        PaidDTO paidByDTO = new PaidDTO();
        paidByDTO.setId(1L);

        Paid paidFor = new Paid();
        paidFor.setId(1L);

        PaidDTO paidForDTO = new PaidDTO();
        paidForDTO.setId(1L);

        entity1.setId(1L);
        entity1.setName("myName1");
        entity1.setEvent(event);
        entity1.setCurrency(currency);
        entity1.setAddress(address);
        entity1.setCategory(category);
        entity1.setPaidBy(paidBy);
        entity1.setPaidFor(paidFor);

        entityDTO1.setId(1L);
        entityDTO1.setName("myName1");
        entityDTO1.setEvent(eventDTO);
        entityDTO1.setCurrency(currencyDTO);
        entityDTO1.setAddress(addressDTO);
        entityDTO1.setCategory(categoryDTO);
        entityDTO1.setPaidBy(paidByDTO);
        entityDTO1.setPaidFor(paidForDTO);

        when(eventService.getById(event.getId())).thenReturn(Optional.of(event));
        when(currencyService.getById(currency.getId())).thenReturn(Optional.of(currency));
        when(addressService.getById(address.getId())).thenReturn(Optional.of(address));
        when(categoryService.getById(category.getId())).thenReturn(Optional.of(category));
        when(paidService.getById(paidBy.getId())).thenReturn(Optional.of(paidBy));
        when(paidService.getById(paidFor.getId())).thenReturn(Optional.of(paidFor));

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        when(service.update(entity1, entity1.getId())).thenReturn(entity1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put(endpoint + "/" + entityDTO1.getId() )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(entityDTO1));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("myName1")));

    }

    @Test
    @Order(5)
    public void delete() throws Exception {

        entity1.setId(1L);

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(endpoint + "/" + entity1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void exceptions_create_HttpMessageNotReadableException() throws Exception {

        when(service.create(entity1)).thenReturn(entity1);

        String content = "{" +
                "\"name\":\"Euro\"," +
                "\"code\":\"EUR\"," +
                "\"country\":\"Euro Member\"," +
                "\"symbol\":\"20ac\"," +
                "}";
//        System.out.println("content : " + content);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(endpoint)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    System.out.printf(result.getResolvedException().toString());
                    assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException);
                });

    }

//    @Test
//    @Order(7)
//    public void exceptions_create_MethodArgumentNotValidException() throws Exception {
//
//        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(endpoint)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(asJsonString(entityDTO1))
//                ;
//
//        mockMvc.perform(mockHttpServletRequestBuilder)
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    System.out.printf(result.getResolvedException().toString());
//                    assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
//                });
//
//    }

    @Test
    @Order(8)
    public void exceptions_create_HttpMediaTypeNotSupportedException() throws Exception {

        when(service.create(entity1)).thenReturn(entity1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_XML)
                .content(asJsonString(entity1));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> {
                    System.out.printf(result.getResolvedException().toString());
                    assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException);
                });

    }

    @Test
    @Order(9)
    public void exceptions_create_MethodArgumentTypeMismatchException() throws Exception {

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(endpoint + "/test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    //System.out.printf(result.getResolvedException().toString());
                    assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException);
                });

    }

    @Test
    @Order(10)
    public void exceptions_create_ConstraintViolationException() throws Exception {

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(endpoint + "/0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    //System.out.printf(result.getResolvedException().toString());
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException);
                });

    }

    @Test
    @Order(11)
    public void exceptions_getById_EntityNotFoundException() throws Exception {

        entity1.setId(1L);
        entity2.setId(2L);

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(endpoint + "/" + entity2.getId() )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));

    }

    @Test
    @Order(12)
    public void exceptions_getById_HttpRequestMethodNotSupportedException() throws Exception {

        entity1.setId(1L);

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.patch(endpoint + "/" + entity1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    System.out.println(result.getResolvedException().toString());
                    assertTrue(result.getResolvedException() instanceof HttpRequestMethodNotSupportedException);
                });

    }

    @Test
    @Order(13)
    public void exceptions_getById_NoHandlerFoundException() throws Exception {

        entity1.setId(1L);

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(endpoint + "extrastuff")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof NoHandlerFoundException);
                });

    }

    @Test
    @Order(14)
    public void exceptions_update_MethodArgumentTypeMismatchException_Test() throws Exception {

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(endpoint + "/null" )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(entity1));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    @Order(15)
    public void exceptions_delete_NotFound() throws Exception {

        entity1.setId(1L);

        entityDTO2.setId(2L);

        when(service.getById(1L)).thenReturn(java.util.Optional.of(entity1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(endpoint + "/" + entityDTO2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Activity was not found for parameters {id=2}", result.getResolvedException().getMessage()));

    }

}
