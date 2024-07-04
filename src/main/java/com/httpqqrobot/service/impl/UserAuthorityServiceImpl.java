package com.httpqqrobot.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.httpqqrobot.entity.UserAuthority;
import com.httpqqrobot.mapper.UserAuthorityMapper;
import com.httpqqrobot.service.IUserAuthorityService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority> implements IUserAuthorityService {

}
