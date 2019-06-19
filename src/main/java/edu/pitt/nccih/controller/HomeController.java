package edu.pitt.nccih.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.pitt.nccih.models.File;
import edu.pitt.nccih.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
//            serializeDefinitions();
            Map<String, String> phrases = new HashMap();
            createPreAnnotations(phrases);

            //load html file into page
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

    private void createPreAnnotations(Map<String, String> phrases) throws IOException {
        List<String> acceptedSemanticTypes = new ArrayList<>();
        buildAcceptedSemanticTypesList(acceptedSemanticTypes);
        getPhrasesFromJson(phrases, acceptedSemanticTypes);

    }

    private void buildAcceptedSemanticTypesList(List<String> list) throws FileNotFoundException {
        Scanner s = new Scanner(new java.io.File(ANNOTATOR_DIR + "t1.txt"));
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
    }

    private void getPhrasesFromJson(Map<String, String> phrases, List<String> acceptedSemanticTypes) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "jsonOfHtml.json")));
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray utterances = jsonObject.get("AllDocuments").getAsJsonArray().get(0).getAsJsonObject().get("Document").getAsJsonObject().get("Utterances").getAsJsonArray();

        for (int i = 0; i < utterances.size(); i++) {
            JsonObject utteranceObject = utterances.get(i).getAsJsonObject();
            JsonArray phrasesArray = utteranceObject.get("Phrases").getAsJsonArray();
            for (int j = 0; j < phrasesArray.size(); j++) {
                JsonObject phraseObject = phrasesArray.get(j).getAsJsonObject();
                JsonArray mappings = phraseObject.getAsJsonArray("Mappings");
                if (mappings.size() > 0) {
                    JsonArray mappingCandidates = mappings.get(0).getAsJsonObject().get("MappingCandidates").getAsJsonArray();
                    for (JsonElement mappingCandidate : mappingCandidates) {
                        //check if phrase is of allowed semantic types
                        JsonArray semTypes = mappingCandidate.getAsJsonObject().get("SemTypes").getAsJsonArray();
                        for (JsonElement semElement : semTypes) {
                            //need to remove excess quotes
                            if (acceptedSemanticTypes.contains(semElement.toString().replace("\"", ""))) {
                                String cui = mappingCandidate.getAsJsonObject().get("CandidateCUI").toString().replace("\"", "");
                                JsonArray matchedWords = mappingCandidate.getAsJsonObject().get("MatchedWords").getAsJsonArray();
                                Type type = new TypeToken<List<String>>() {
                                }.getType();
                                String phraseText = new Gson().fromJson(matchedWords.toString(), type).toString().replaceAll("\\[|\\]|,|\\s", " ").trim();

                                phrases.put(phraseText, cui);
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    private void serializeDefinitions() {
        Map<String, String> map = new HashMap<>();
        String csvFile = "/Users/mas400/select_CUI__STR_from_MRCONSO.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] definition = line.split(cvsSplitBy);
                map.put(definition[0], definition[1]);

            }

            FileOutputStream fos = new FileOutputStream(ANNOTATOR_DIR + "definitions.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.ser");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, String> deserializeDefinitions() {
        Map<String, String> map = null;
        try {
            FileInputStream fis = new FileInputStream(ANNOTATOR_DIR + "definitions.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
