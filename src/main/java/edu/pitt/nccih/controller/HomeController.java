package edu.pitt.nccih.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import edu.pitt.nccih.models.EnglishPhrase;
import edu.pitt.nccih.models.File;
import edu.pitt.nccih.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.pitt.nccih.controller.AnnotatorController.ANNOTATOR_DIR;

@Controller
public class HomeController {
    private static Map<String, String> definitions;
    private static Map<String, EnglishPhrase> englishDefinitions;

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
//            serializeEnglishDefinitions();
            Map<String, String> phrases = new HashMap();
            createPreAnnotations(phrases);

            //load html file into page
            File file = fileService.findByUri(uri);

            //check what the type of file is
            String contents = "";
            if (FilenameUtils.getExtension(file.getUrl()).equals("html")) {
                model.addAttribute("contentIsFile", true);
                contents = Files.toString(new java.io.File(ANNOTATOR_DIR + "html/" + file.getUrl()), Charsets.UTF_8);
            }
            if (FilenameUtils.getExtension(file.getUrl()).equals("mp4")) {
                model.addAttribute("contentIsVideo", true);
                model.addAttribute("subtitles", FilenameUtils.getBaseName(file.getUrl()) + ".vtt");
                contents = file.getUrl();
            }


            model.addAttribute("addPreAnnotation", false);
            model.addAttribute("phrases", phrases);
            model.addAttribute("fileContents", contents);
            model.addAttribute("uri", uri);
            return "viewer";
        } catch (Exception e) {
            return "resourceNotFound";
        }
    }

    private void createPreAnnotations(Map<String, String> phrases) throws IOException {
        if (definitions == null) {
            definitions = deserializeDefinitions();
        }
        if (englishDefinitions == null) {
            englishDefinitions = deserializeEnglishDefinitions();
        }
        List<String> acceptedSemanticTypes = new ArrayList<>();
        List<String[]> reportData = new ArrayList<>();
        reportData.add(new String[]{"Word", "Definition", "CUI", "Semantic Type", "Terminology"});

        buildAcceptedSemanticTypesList(acceptedSemanticTypes);
        getPhrasesFromJson(phrases, acceptedSemanticTypes, reportData);
        writeReport(reportData);
    }

    private void buildAcceptedSemanticTypesList(List<String> list) throws FileNotFoundException {
        Scanner s = new Scanner(new java.io.File(ANNOTATOR_DIR + "t1.txt"));
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
    }

    private void getPhrasesFromJson(Map<String, String> phrases, List<String> acceptedSemanticTypes, List<String[]> reportData) throws IOException {
        String json = new String(java.nio.file.Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "FibrodysplasiaJson.json")));
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
                                String phraseText = new Gson().fromJson(matchedWords.toString(), type).toString().replaceAll("\\[|\\]|\\s", "").replace(",", " ").trim();

                                if (definitions.containsKey(cui)) {
                                    if (!phrases.containsKey(phraseText)) {
                                        phrases.put(phraseText, definitions.get(cui));
                                        reportData.add(new String[]{phraseText, definitions.get(cui), cui, semElement.toString().replace("\"", ""), mappingCandidate.getAsJsonObject().get("Sources").getAsJsonArray().toString()});
                                    }
                                }
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
        String csvFile = "/Users/mas400/dev/annotator-file-dir/select_CUI__DEF_from_MRDEF_where_SAB____.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] definition = line.split(cvsSplitBy, 2);
                map.put(definition[0], definition[1].replace("\"", "").replace("<p>", "").replace("</p>", ""));

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

    private void writeReport(List<String[]> reportData) {
        try {
            java.io.File csvOutputFile = new java.io.File(ANNOTATOR_DIR + "report.csv");
            try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
                reportData.stream()
                        .map(this::convertToCSV)
                        .forEach(pw::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private Map<String, String> deserializeDefinitions() {
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

    private void serializeEnglishDefinitions() {
        Map<String, EnglishPhrase> map = new HashMap<>();
        String csvFile = "/Users/mas400/dev/annotator-file-dir/Andy Biemiller Words Worth Teaching Alphabetical and Ratings List.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine(); // skip first line (header)
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] definition = line.split(cvsSplitBy);
                EnglishPhrase englishPhrase = new EnglishPhrase();
                englishPhrase.setDefinition(definition[1]);
                englishPhrase.setDifficulty(definition[2]);
                map.put(definition[0], englishPhrase);

            }

            FileOutputStream fos = new FileOutputStream(ANNOTATOR_DIR + "englishDefinitions.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in englishDefinitions.ser");

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

    private Map<String, EnglishPhrase> deserializeEnglishDefinitions() {
        Map<String, EnglishPhrase> map = null;
        try {
            FileInputStream fis = new FileInputStream(ANNOTATOR_DIR + "englishDefinitions.ser");
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
