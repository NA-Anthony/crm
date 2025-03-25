package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.service.csv.CsvImportService;
import site.easy.to.build.crm.exception.CsvImportException;
import site.easy.to.build.crm.service.database.DatabaseService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.service.csv.ImportResult;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/csv")
public class CsvImportController {
    private final AuthenticationUtils authenticationUtils;
    private final CsvImportService csvImportService;
    private final DatabaseService databaseService;

    public CsvImportController(AuthenticationUtils authenticationUtils,
                               CsvImportService csvImportService,
                               DatabaseService databaseService) {
        this.authenticationUtils = authenticationUtils;
        this.csvImportService = csvImportService;
        this.databaseService = databaseService;
    }

    @GetMapping
    public String showImportPage(Model model) {
        List<String> tables = databaseService.getFilteredTables();
        model.addAttribute("tables", tables);
        return "csv/import-csv";
    }

    @PostMapping("/import")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            ImportResult result = csvImportService.importCsv(file);

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("errors", result.getErrors());
            }

            result.getSuccessCountByTable().forEach((table, count) -> {
                redirectAttributes.addFlashAttribute(
                        "message",
                        String.format("%d enregistrements importés avec succès dans %s", count, table)
                );
            });

        } catch (CsvImportException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur technique lors de l'import: " + e.getMessage());
        }

        return "redirect:/csv";
    }

    @ResponseBody
    @PostMapping("/api/import")
    public ResponseEntity<?> handleApiImport(@RequestParam("file") MultipartFile file) {
        try {
            ImportResult result = csvImportService.importCsv(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "Erreur d'import",
                            "details", e.getMessage()
                    ));
        }
    }
}