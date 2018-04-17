package com.meetup.meetup.rest.controller;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.impl.UserDaoImpl;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.rest.controller.errors.DatabaseWorkException;
import com.meetup.meetup.rest.controller.errors.EmailAlreadyUsedException;
import com.meetup.meetup.rest.controller.errors.LoginAlreadyUsedException;
import com.meetup.meetup.rest.controller.errors.MD5EncodingException;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.MailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MailService mailService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) {
        System.out.println("Controller"+user);
        if (null != userDao.findByLogin(user.getLogin()))  //checking if user exist in system
            throw new LoginAlreadyUsedException();

        if (null != userDao.findByEmail(user.getEmail())) //checking if email exist in system
            throw new EmailAlreadyUsedException();

        String md5Pass = HashMD5.hash(user.getPassword());

        if(null == md5Pass)
            throw new MD5EncodingException();

        if (userDao.insert(user) == -1) //checking adding to DB
            throw new DatabaseWorkException();

        mailService.sendMail(user.getEmail(), "Registration successfully", String.format(MailService.templateRegister, user.getName(), user.getLogin(), user.getPassword()));
        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }
}