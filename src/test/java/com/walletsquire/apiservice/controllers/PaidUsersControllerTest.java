package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.mappers.CategoryMapperQualifier;
import com.walletsquire.apiservice.mappers.PaidMapper;
import com.walletsquire.apiservice.mappers.PaidUsersMapper;
import com.walletsquire.apiservice.mappers.UserMapper;
import com.walletsquire.apiservice.services.*;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.hu.Ha;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@WebMvcTest( controllers = PaidUsersController.class )
@ComponentScan(basePackageClasses = PaidUsersMapper.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("dev")
@TestMethodOrder(OrderAnnotation.class)
public class PaidUsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PaidUsersMapper mapper;

    @Autowired
    PaidUsersController controller;

    String endpoint = "/api/v1" + controller.endpoint;

    // mock objects
    @MockBean
    PaidUsersService service;

    @MockBean
    CategoryMapperQualifier categoryMapperQualifier;

    @MockBean
    UserService userService;

    @MockBean
    PaidService paidService;

    @MockBean
    AddressService addressService;

    @MockBean
    CurrencyService currencyService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    PaidMapper paidMapper;

    @MockBean
    UserMapper userMapper;

    /* entities/DTOs go here for testing */
    PaidUsers entity1 = new PaidUsers();
    PaidUsers entity2 = new PaidUsers();
    PaidUsersDTO entityDTO1 = new PaidUsersDTO();
    PaidUsersDTO entityDTO2 = new PaidUsersDTO();

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

        Paid paid1 = new Paid();
        paid1.setId(1L);

        User user1 = new User();
        user1.setId(1L);

        PaidDTO paidDTO = new PaidDTO();
        paidDTO.setId(1L);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        entity1.setId(1L);
        entity1.setAmount(BigDecimal.valueOf(1));
        entity1.setPaid(paid1);
        entity1.setUser(user1);

        entityDTO1.setId(1L);
        entityDTO1.setAmount(BigDecimal.valueOf(1));
        entityDTO1.setPaid(paidDTO);
        entityDTO1.setUser(userDTO);

        when(paidService.getById(1L)).thenReturn(Optional.of(paid1));
        when(userService.getById(1L)).thenReturn(Optional.of(user1));

        when(service.create(entity1)).thenReturn(entity1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(entityDTO1))
                ;

//         use this to print out the content
//        MvcResult result = mockMvc.perform(mockHttpServletRequestBuilder)
//                .andExpect(status().isCreated())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        String content = result.getResponse().getContentAsString();
//        System.out.println("content : ->" + content + "<-");

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.amount", equalTo(1)))
                .andDo(print())
                ;

    }

    @Test
    @Order(2)
    public void getById() throws Exception {

        entity1.setId(1L);
        entity1.setAmount(BigDecimal.valueOf(1));

        entityDTO1.setId(1L);
        entityDTO1.setAmount(BigDecimal.valueOf(1));

        when(service.getById(entity1.getId())).thenReturn(java.util.Optional.of(entity1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint + "/" + entity1.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.amount", equalTo(1)));

    }

    @Test
    @Order(3)
    public void getAll() throws Exception {

        entity1.setAmount(BigDecimal.valueOf(1));

        entityDTO1.setId(1L);
        entityDTO1.setAmount(BigDecimal.valueOf(1));

        List<PaidUsers> entitys = new ArrayList<>(Arrays.asList(entity1, entity2));

        when(service.getAll()).thenReturn(entitys);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", equalTo(1)));

    }

    @Test
    @Order(4)
    public void update() throws Exception {

        entity1.setId(1L);
        entity1.setAmount(BigDecimal.valueOf(1));

        entityDTO1.setId(1L);
        entityDTO1.setAmount(BigDecimal.valueOf(1));

        when(service.getById(1L)).thenReturn(Optional.of(entity1));

        when(service.update(any(PaidUsers.class), any(PaidUsersDTO.class), any(Long.class))).thenReturn(entity1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put(endpoint + "/" + entityDTO1.getId() )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(entityDTO1));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.amount", is(1)));

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
                .andExpect(result -> assertEquals("PaidUsers was not found for parameters {id=2}", result.getResolvedException().getMessage()));

    }

}
