package com.aisys.cron;

import com.aisys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UserCountUpdate {
    @Autowired
    private UserService userService;

    @Scheduled(cron="0 0 0 * * *") // 秒 分 时 日 月 星 年(可不填)
    public void userCountUpdate() {
        userService.updateUserCount();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void rmOldDir() {
        File file = new File(".");
        File realPath=file.getAbsoluteFile();
    }

}
