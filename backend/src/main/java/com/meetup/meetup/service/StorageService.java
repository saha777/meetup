package com.meetup.meetup.service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.frontend.detailed.FileUploadException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.meetup.meetup.Keys.Key.EXCEPTION_FILE_UPLOAD;


@Service
@PropertySource("classpath:links.properties")
@PropertySource("classpath:strings.properties")
public class StorageService {

    @Autowired
    private Environment env;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDao userDao;

    private Path rootLocation ;

    public User store(MultipartFile file) {
        rootLocation = Paths.get(env.getProperty("profile.img.link"));
        User user = authenticationFacade.getAuthentication();
        String inFileFormat = "."+file.getOriginalFilename().split("\\.")[1];
        user.setImgPath(env.getProperty("remote.img.link")+user.getId()+inFileFormat);
        userDao.update(user);
        try {
            Files.deleteIfExists(this.rootLocation.resolve(user.getId()+inFileFormat));
            Files.copy(file.getInputStream(), this.rootLocation.resolve(user.getId()+inFileFormat));
            return user;
        } catch (Exception e) {
            throw new FileUploadException(String.format(env.getProperty(EXCEPTION_FILE_UPLOAD),file.getOriginalFilename()));        }
    }

}