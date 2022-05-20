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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACHI
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
        return "coba/create";
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
        
        return "coba/create";
    }
    
    @RequestMapping (value="/gambar" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") Long id) throws Exception {
	Dummy dum = dummyController.findDummy(id);
	byte[] gambar = dum.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(gambar);
}
}