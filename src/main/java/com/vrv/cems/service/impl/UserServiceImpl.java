package com.vrv.cems.service.impl;

import com.vrv.cems.dao.CemsUserMapper;
import com.vrv.cems.service.ICemsUserService;
import com.vrv.cems.dao.CemsUserMapper;
import com.vrv.cems.service.ICemsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements ICemsUserService {

    @Autowired
    private CemsUserMapper cemsUserDao;//这里可能会报错，但是并不会影响

    @Override
    public List findAllUser(){
       return  cemsUserDao.findAllUser();
    }
}
