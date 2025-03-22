package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.easy.to.build.crm.service.data.DataDeletionService;

@Controller
public class DataDeletionController {

    private final DataDeletionService dataDeletionService;

    @Autowired
    public DataDeletionController(DataDeletionService dataDeletionService) {
        this.dataDeletionService = dataDeletionService;
    }

    @GetMapping("/delete-all-data")
    public String deleteAllData() {
        try {
            dataDeletionService.deleteAllData();
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/erreur";  // Par exemple, une page d'erreur
        }
    }
}

