package com.heihei.ctrl;

import com.heihei.croe.MyStartupRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Administrator on 2017/10/7.
 */
@Controller
public class Login
{
    @GetMapping(value = "/")
    public String toIndex(Model model) {
       // model.addAttribute("datasource", MyStartupRunner.source);
        return "index";
    }
}
