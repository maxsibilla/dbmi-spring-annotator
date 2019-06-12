package edu.pitt.nccih.controller;

import edu.pitt.nccih.models.File;
import edu.pitt.nccih.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Paths;

import static edu.pitt.nccih.controller.AnnotatorController.ANNOTATOR_DIR;

@Controller
public class HomeController {
    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        return "welcome";
    }

    @GetMapping("/annotatorTest")
    public String annotatorTest(Model model, HttpSession session) {
        return "annotatorTest";
    }

    @GetMapping("/view")
    public String view(@RequestParam String uri, Model model, HttpSession session) {
        try {
            File file = fileService.findByUri(uri);
            String contents = new String(Files.readAllBytes(Paths.get(ANNOTATOR_DIR + file.getUrl())));

            model.addAttribute("fileContents", contents);
            model.addAttribute("uri", uri);
            return "viewer";
        } catch (Exception e) {
            return "resourceNotFound";
        }
    }
}
