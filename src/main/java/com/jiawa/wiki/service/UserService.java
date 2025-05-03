package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.User;
import com.jiawa.wiki.domain.UserExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.req.UserLoginReq;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserResetPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.UserLoginResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtil;
import com.jiawa.wiki.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    //Both annotation are valid
    @Resource
    //@Autowired
    private UserMapper userMapper;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(example);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("Total rows: {}", pageInfo.getTotal());
        LOG.info("Total pages: {}", pageInfo.getPages());

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(userList, UserQueryResp.class));

        return pageResp ;
    }

    /**
     * Save the changes made from frontend
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            User userFromDB = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(userFromDB)) {
                // Add a new record
                userMapper.insert(user);
            } else {
                // Username is already existed
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // Update, set username and password to null so make them un-editable
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * Reset the password for a user
     */
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * Login
     */
    public UserLoginResp login(UserLoginReq req) {
        User userDB = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDB)) {
            // Username does not exist
            LOG.info("Username does not exist, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (req.getPassword().equals(userDB.getPassword())) {
                // Login successfully
                UserLoginResp loginResp = CopyUtil.copy(userDB, UserLoginResp.class);
                return loginResp;
            } else {
                // Password wrong
                LOG.info("Username or password error, database: {}, input: {}"
                        , userDB.getPassword(), req.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

}
