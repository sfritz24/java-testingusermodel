package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp()
            throws
            Exception
    {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("testadmin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(4);
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(5);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(6);

        // data, user
        User u2 = new User("testcinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(7);
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(8);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(9);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(10);

        // user
        User u3 = new User("testbarnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(11);
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(12);

        User u4 = new User("testputtat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(13);
        u4.getRoles().add(new UserRoles(u4, r2));

        User u5 = new User("testmisskitty",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(14);
        u5.getRoles().add(new UserRoles(u5, r2));

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
    }

    @After
    public void tearDown()
            throws
            Exception
    {
    }

    @Test
    public void listAllUsers() throws Exception
    {
        String apiUrl = "/users/users";

        Mockito.when(userService.findAll()).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(tr, er);
    }

    @Test
    public void getUserById() throws Exception
    {
        String apiUrl = "/users/user/4";

        Mockito.when(userService.findUserById(4)).thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(tr, er);
    }

    @Test
    public void getUserByIdNotFound() throws Exception
    {
        String apiUrl = "/users/user/50";

        Mockito.when(userService.findUserById(50)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals(tr, er);
    }

    @Test
    public void getUserByName() throws Exception
    {
        String apiUrl = "/users/user/name/testadmin";

        Mockito.when(userService.findByName("testadmin")).thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(tr, er);
    }

    @Test
    public void getUserByNameNotFound() throws Exception
    {
        String apiUrl = "/users/user/name/test";

        Mockito.when(userService.findByName("test")).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals(tr, er);
    }

    @Test
    public void getUserLikeName() throws Exception
    {
        String apiUrl = "/users/user/name/like/test";

        Mockito.when(userService.findByNameContaining("test")).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(tr, er);
    }

    @Test
    public void addNewUser() throws Exception
    {
        String apiUrl = "/users/user";

        User u5 = new User("testcharlie",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(15);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        u5.getRoles().add(new UserRoles(u5, r2));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u5);

        Mockito.when(userService.save(any(User.class))).thenReturn(u5);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser()
    {
    }

    @Test
    public void updateUser()
    {
    }

    @Test
    public void deleteUserById() throws
            Exception
    {
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }
}