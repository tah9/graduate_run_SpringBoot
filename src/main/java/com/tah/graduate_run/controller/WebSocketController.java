package com.tah.graduate_run.controller;

/**
 * ->  tah9  2021/9/9 14:50
 */

import com.tah.graduate_run.service.WebSocketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/websocket")
public class WebSocketController {


    @GetMapping("/pushone")
    public void pushone() {
        WebSocketService.sendMessage("badao", "公众号:霸道的程序猿");
    }
}
