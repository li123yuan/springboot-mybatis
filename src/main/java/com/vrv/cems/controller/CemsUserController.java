package com.vrv.cems.controller;

import com.vrv.cems.pojo.CemsUser;
import com.vrv.cems.service.ICemsUserService;
import com.vrv.cems.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/user"})
public class CemsUserController {

    @Autowired
    private ICemsUserService userService;

    @RequestMapping(value = {"/findAll"},produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public List getAllUsers(){
       List list =  userService.findAllUser();
       return list;
    }

    @GetMapping("getAllUser")
    public Map<String, Object> getAllUser() {
        List<CemsUser> allUsers = userService.findAllUser();
        if (allUsers.size() == 0) {
            return Json.fail();
        } else {
            return Json.success(allUsers);
        }
    }
}
