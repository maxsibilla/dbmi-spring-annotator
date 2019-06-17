package edu.pitt.nccih.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.ArrayList;
import java.util.List;

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
            List<String> phrases = new ArrayList<>();
            String json = new String(Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "wiki-1.json")));
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            JsonArray utterances = jsonObject.get("AllDocuments").getAsJsonArray().get(0).getAsJsonObject().get("Document").getAsJsonObject().get("Utterances").getAsJsonArray();

            for (int i = 0; i < utterances.size(); i++) {
                JsonObject utteranceObject = utterances.get(i).getAsJsonObject();
                JsonArray phrasesArray = utteranceObject.get("Phrases").getAsJsonArray();
                for (int j = 0; j < phrasesArray.size(); j++) {
                    JsonObject phraseObject = phrasesArray.get(j).getAsJsonObject();
                    phrases.add(phraseObject.get("PhraseText").toString());
                }
            }

            File file = fileService.findByUri(uri);
            String contents = new String(Files.readAllBytes(Paths.get(ANNOTATOR_DIR + file.getUrl())));

            model.addAttribute("phrases", phrases);
            model.addAttribute("fileContents", contents);
            model.addAttribute("uri", uri);
            return "viewer";
        } catch (Exception e) {
            return "resourceNotFound";
        }
    }
}
