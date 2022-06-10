/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.ktp.project.ktp.dummy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author USER
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    //@ResponseBody
    public String getDummy (Model model) {
    int record = dummyController.getDummyCount();
        String result = "";
        try{
            data = dummyController.findDummyEntities().subList(0, record);
        }
        catch (Exception e){
            result=e.getMessage();
        }
        
        model.addAttribute("godummy", data);
         model.addAttribute("record", record);
         
        return "dummy";    
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "create";
    }
    
     @PostMapping(value="/newdata", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(HttpServletRequest data,@RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        Long iid = Long.parseLong(id);
        String tanggal_lahir = data.getParameter("tanggal_lahir");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal_lahir);
        byte[] image = file.getBytes();
        dumdata.setId(iid);
        dumdata.setTanggalLahir(date);
        dumdata.setGambar(image);
        dummyController.create(dumdata);
        
        return "create";
    }
    
    @RequestMapping (value="/gambar" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") Long id) throws Exception {
	Dummy dum = dummyController.findDummy(id);
	byte[] gambar = dum.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(gambar);
    }
    
    @GetMapping (value="/delete/{id}")
    public String deleteDummy(@PathVariable("id") Long id) throws Exception{
        dummyController.destroy(id);
        return "redirect:/read";
    }
    
    @RequestMapping("/edit/{id}")
    //@ResponseBody
    public String updateDummy (Model model, @PathVariable("id") Long id) throws Exception {
        Dummy data = dummyController.findDummy(id);
        model.addAttribute("godummy", data);         
        return "/update";    
    }
    
    @PostMapping(value="/update", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String editDummy(HttpServletRequest data,@RequestParam("gambar") MultipartFile file, HttpServletResponse httpResponse) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        Long iid = Long.parseLong(id);
        String tanggal_lahir = data.getParameter("tanggal_lahir");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal_lahir);
        byte[] image = file.getBytes();
        dumdata.setId(iid);
        dumdata.setTanggalLahir(date);
        dumdata.setGambar(image);
        dummyController.edit(dumdata);
        
        httpResponse.sendRedirect("read");
        return null;
    }
}