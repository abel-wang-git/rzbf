package com.heihei.ctrl;

import com.heihei.croe.Shell;
import com.heihei.po.Datasoruce;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */
@Controller
@RequestMapping(value = "/zb")
public class Zb {
    @PostMapping(value = "/delayed")
    public String delayed(Model model,@RequestParam(defaultValue = "") String name,@RequestParam(defaultValue = "") Datasoruce s){
        List<String[]> list = new ArrayList<>();
        Shell sshExecutor = new Shell(s.getIP(), s.getSysUser(), s.getSysPwd());
        String c= sshExecutor.execute("source /home/oracle/.bash_profile&&/u01/ogg/ggsci <<EOF \n" +
                "lag  "+name+" \n" +
                "EOF");

        String[] strings= c.split("\\n");
       model.addAttribute("delayed",strings[14]);

        return "/process";
    }

}
