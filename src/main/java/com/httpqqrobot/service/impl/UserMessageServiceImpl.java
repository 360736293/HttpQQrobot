package com.httpqqrobot.service.impl;

import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.mapper.UserMessageMapper;
import com.httpqqrobot.service.IUserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mfliu
 * @since 2025-01-20
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

}
