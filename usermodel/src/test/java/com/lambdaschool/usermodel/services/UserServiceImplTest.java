package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import javax.swing.*;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = UserModelApplication.class)
public class UserServiceImplTest
{
    @Autowired
    private UserService userService;

    @Before
    public void setUp()
            throws
            Exception
    {
        MockitoAnnotations.initMocks(this);

//        List<User> myList = userService.findAll();
//        for (User u : myList)
//        {
//            System.out.println(u.getUserid() + " " + u.getUsername());
//        }
    }

    @After
    public void tearDown()
            throws
            Exception
    {
    }

    @Test
    public void afindUserById()
    {
        assertEquals("testcinnamon", userService.findUserById(7).getUsername());
    }

    @Test (expected = EntityNotFoundException.class)
    public void afindUserByIdNotFound()
    {
        assertEquals("testcinnamon", userService.findUserById(77).getUsername());
    }

    @Test
    public void afindByNameContaining()
    {
        assertEquals(5, userService.findByNameContaining("test").size());
    }


    @Test
    public void afindAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void zdelete()
    {
        userService.delete(7);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void afindByName()
    {
        assertEquals(7, userService.findByName("testcinnamon").getUserid());
    }

    @Test (expected = EntityNotFoundException.class)
    public void afindByNameNotFound()
    {
        assertEquals(77, userService.findByName("test").getUserid());
    }

    @Test
    public void save()
    {
    }

    @Test
    public void update()
    {
    }

    @Test
    public void zdeleteAll()
    {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}