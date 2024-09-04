package com.acc.controller.system.system;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.config.AccConfig;
import com.acc.core.entity.User;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.DateUtils;
import com.acc.core.utils.ShiroUtils;
import com.acc.core.utils.StringUtils;
import com.acc.core.utils.file.FileUploadUtils;
import com.acc.core.utils.file.MimeTypeUtils;
import com.acc.service.PasswordService;
import com.acc.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 */
@Controller
@RequestMapping("/system/user/profile")
@Slf4j
@Api(tags = "个人信息管理")
public class ProfileController extends BaseController {

    private String prefix = "system/user/profile";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    /**
     * 个人信息
     */
    @GetMapping
    @ApiOperation("获取个人信息页面")
    public String profile(ModelMap mmap) {
        User user = getUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    @ApiOperation("校验密码")
    public boolean checkPassword(String password) {
        User user = getUser();
        return passwordService.matches(user, password);
    }

    @GetMapping("/resetPwd")
    @ApiOperation("获取修改密码页面")
    public String resetPwd(ModelMap mmap) {
        User user = getUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    @ApiOperation("修改密码")
    public AjaxResult resetPwd(String oldPassword, String newPassword) {
        User user = getUser();
        if (!passwordService.matches(user, oldPassword)) {
            return error("修改密码失败，旧密码错误");
        }
        if (passwordService.matches(user, newPassword)) {
            return error("新密码不能与旧密码相同");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
        user.setPwdUpdateDate(DateUtils.getNowDate());
        if (userService.resetUserPwd(user) > 0) {
            setUser(userService.selectUserById(user.getUserId()));
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    @ApiOperation("获取修改用户页面")
    public String edit(ModelMap mmap) {
        User user = getUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    @ApiOperation("获取修改头像页面")
    public String avatar(ModelMap mmap) {
        User user = getUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/avatar";
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("修改并保存用户")
    public AjaxResult update(User user) {
        User currentUser = getUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhone()) && !userService.checkPhoneUnique(currentUser)) {
            return error("修改用户'" + currentUser.getLoginName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return error("修改用户'" + currentUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserInfo(currentUser) > 0) {
            setUser(userService.selectUserById(currentUser.getUserId()));
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    @ApiOperation("修改并保存头像")
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        User currentUser = getUser();
        try {
            if (!file.isEmpty()) {
                String avatar = FileUploadUtils.upload(AccConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
                currentUser.setAvatar(avatar);
                if (userService.updateUserInfo(currentUser) > 0) {
                    setUser(userService.selectUserById(currentUser.getUserId()));
                    return success();
                }
            }
            return error();
        } catch (Exception e) {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
}
