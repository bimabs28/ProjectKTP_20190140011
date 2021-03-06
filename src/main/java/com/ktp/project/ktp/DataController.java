/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.ktp.project.ktp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@Controller
public class DataController {
    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();
    @RequestMapping("/main")
    public String getMain(){
        return "menu";
    }
    @RequestMapping("/data")
    public String getDataKTP(Model model) {
        int record = datactrl.getDataCount();
        String result = "";
        try{
            newdata = datactrl.findDataEntities().subList(0,record);
        }
        catch(Exception e){
            result=e.getMessage();
        }
        model.addAttribute("goData", newdata);
        model.addAttribute("record", record);
        return "database";
    }
    @RequestMapping("/edit")
    public String doEdit(){
        return "editktp";
    }
}