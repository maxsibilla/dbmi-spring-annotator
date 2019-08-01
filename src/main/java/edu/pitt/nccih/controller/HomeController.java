package edu.pitt.nccih.controller;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.pitt.nccih.auth.model.User;
import edu.pitt.nccih.auth.service.UserService;
import edu.pitt.nccih.models.File;
import edu.pitt.nccih.models.*;
import edu.pitt.nccih.service.AnnotationService;
import edu.pitt.nccih.service.AnnotationTrackerService;
import edu.pitt.nccih.service.FileService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.pitt.nccih.controller.AnnotatorController.ANNOTATOR_DIR;
import static edu.pitt.nccih.controller.AnnotatorController.isUserEditor;

@Controller
public class HomeController {
    public static Map<String, String> definitions;
    public static Map<String, EnglishPhrase> englishDefinitions;
    private static Map<String, List<UserFileInfo>> userFileInformation;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private AnnotationTrackerService annotationTrackerService;

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        if (userFileInformation == null || userFileInformation.size() == 0) {
            userFileInformation = new HashMap<>();
            createUserFileInformation();
        }
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());
            model.addAttribute("userFileInfoList", userFileInformation.get(user.getUsername()));
            if (user.getUncompletedFiles() == null && user.getNextFileToComplete() == null) {
                ArrayList<String> uncompletedFiles = new ArrayList<>();
                for (UserFileInfo userFileInfo : userFileInformation.get(user.getUsername())) {
                    uncompletedFiles.add("preTest" + userFileInfo.getFilename());
                    uncompletedFiles.add(userFileInfo.getFilename());
                    uncompletedFiles.add("postTest" + userFileInfo.getFilename());
                }
                String nextFileToComplete = uncompletedFiles.get(0);
                uncompletedFiles.remove(0);
                user.setNextFileToComplete(nextFileToComplete);
                user.setUncompletedFiles(uncompletedFiles);
                userService.update(user);
            }
            model.addAttribute("nextFileToComplete", user.getNextFileToComplete());
            model.addAttribute("uncompletedFiles", user.getUncompletedFiles());
            if (isUserEditor(user)) {
                model.addAttribute("isEditor", true);
            }
            return "welcome";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    @ResponseBody
    public void markUriAsCompleted(@RequestParam String uri, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());

            if (uri.equals(user.getNextFileToComplete())) {
                ArrayList<String> uncompletedFiles = user.getUncompletedFiles();
                String nextFileToComplete = uncompletedFiles.get(0);
                uncompletedFiles.remove(0);
                user.setNextFileToComplete(nextFileToComplete);
                user.setUncompletedFiles(uncompletedFiles);
                userService.update(user);
            }
            return;
        }
        response.setStatus(401);
    }

    @GetMapping("/resetFiles")
    public String resetFiles(HttpSession session) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());
            user.setNextFileToComplete(null);
            user.setUncompletedFiles(null);
            userService.update(user);
        }
        return "redirect:/";
    }

    @GetMapping("/analytics")
    public String analytics(Model model, HttpSession session) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());
            if (isUserEditor(user)) {
                List<AnnotationTracker> annotationTrackers = annotationTrackerService.findAll();
                model.addAttribute("annotationTrackers", annotationTrackers);
                model.addAttribute("isEditor", true);

                return "analytics";
            } else {
                return "unauthorizedRequest";
            }
        }

        return "unauthorizedRequest";
    }

    @GetMapping("/annotatorTest")
    public String annotatorTest(Model model, HttpSession session) {
        return "annotatorTest";
    }

    @GetMapping("/view")
    public String view(@RequestParam String uri, @RequestParam boolean showAnnotator, Model model, HttpSession session) {
        try {
            //To create pre-annotation set variable "preAnnotationType" to the proper tag this will be creating (enlgish or scientific) and set model attribute "addPreAnnotation" to true. The file will be a JSON file produced by metamap for science words and a TXT file of the page for english words. The metamap json file contains the phrases that we need to extract while the english words will be compared against Andy Biemiller Words Worth Teaching Alphabetical and Ratings List. This helps us generate a list of potential words that will later be refined.

//            serializeDefinitions();
//            serializeEnglishDefinitions();

//            Map<String, String> phrases = new HashMap();
//            String preAnnotationType = "scientific";
//            String preAnnotationFile = "GeneticCodeJson.json";
//            String subtitleFile = "GeneticCodeText.txt";
//            createPreAnnotations(phrases, preAnnotationType, preAnnotationFile, subtitleFile, uri);
//            model.addAttribute("phrases", phrases);
//            model.addAttribute("addPreAnnotation", true);

            List<Annotation> annotations = new ArrayList<>();
            String annotationFile = "punnettPreAnnotationEnglish.csv";
            String preAnnotationType = "english";
            createAnnotations(annotations, annotationFile);
            model.addAttribute("annotations", annotations);
            model.addAttribute("addAnnotation", false);

//            String subtitleFile = "GeneticCodeText.txt";
//            createSubtitlePreAnnotations(annotations, preAnnotationType, subtitleFile, uri);

            model.addAttribute("preAnnotationType", preAnnotationType);

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
                model.addAttribute("startTime", file.getStartTime());
                model.addAttribute("endTime", file.getEndTime());
                contents = file.getUrl();
            }

            if (showAnnotator == false) {
                model.addAttribute("disableAnnotations", true);
            } else {
                model.addAttribute("disableAnnotations", false);
            }


            if (Interceptor.ifLoggedIn(session)) {
                User user = userService.findByUsername(session.getAttribute("username").toString());
                if (isUserEditor(user)) {
                    model.addAttribute("readOnly", false);
                } else {
                    model.addAttribute("readOnly", true);
                }

            } else {
                model.addAttribute("readOnly", true);
            }
            model.addAttribute("fileContents", contents);
            model.addAttribute("uri", uri);
            return "viewer";
        } catch (Exception e) {
            return "resourceNotFound";
        }
    }
//
//    private void createPreAnnotations(Map<String, String> phrases, String preAnnotationType, String preAnnotationFile, String subtitleFile, String uri) throws IOException, ParseException {
//        if (definitions == null) {
//            definitions = deserializeDefinitions();
//        }
//        if (englishDefinitions == null) {
//            englishDefinitions = deserializeEnglishDefinitions();
//        }
//        List<String> acceptedSemanticTypes = new ArrayList<>();
//        List<String[]> reportData = new ArrayList<>();
//
//        if (preAnnotationType.equals("scientific")) {
//            reportData.add(new String[]{"Word", "Definition", "CUI", "Semantic Type", "Terminology"});
//            buildAcceptedSemanticTypesList(acceptedSemanticTypes);
//            getPhrasesFromJson(phrases, acceptedSemanticTypes, reportData, preAnnotationFile);
//        }
//
//        if (preAnnotationType.equals("english")) {
//            reportData.add(new String[]{"Word", "Definition", "Rating"});
//            getPhrasesFromEnglishWordListing(phrases, reportData, preAnnotationFile);
//        }
//
////        if (subtitleFile != null) {
////            createSubtitlePreAnnotations(phrases, preAnnotationType, subtitleFile, uri);
////        }
//
//        writeReport(reportData);
//    }

    private void createSubtitlePreAnnotations(List<Annotation> annotations, String preAnnotationType, String subtitleFile, String uri) throws IOException, ParseException {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(ANNOTATOR_DIR + "pre-annotation/" + subtitleFile));
            String line = reader.readLine();
            String startTime = "";
            String fullLine = "";
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
                if (line.contains("-->")) {
                    fullLine = "";
                    String[] times = line.split("-->");
                    Date date = new SimpleDateFormat("hh:mm:ss.SSS").parse(times[0]);
                    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendar.setTime(date);
                    int start = calendar.get(Calendar.HOUR) * 3600;
                    start += calendar.get(Calendar.MINUTE) * 60;
                    start += calendar.get(Calendar.SECOND);
                    startTime = start + "." + calendar.get(Calendar.MILLISECOND);
                    DecimalFormat decimalFormat = new DecimalFormat("0.###");
                    startTime = decimalFormat.format(Double.valueOf(startTime));
                } else {
                    fullLine += line;
                    for (Annotation annotation : annotations) {
                        if (line.contains(annotation.getQuote())) {
                            Annotation newAnnotation = new Annotation();
                            newAnnotation.setQuote(annotation.getQuote());
                            newAnnotation.setText(annotation.getText());
                            newAnnotation.setFigure(annotation.getFigure());
                            newAnnotation.setVideo(annotation.getVideo());
                            Range range = new Range();
                            int start = fullLine.indexOf(newAnnotation.getQuote());
                            range.setStartOffset(start);
                            range.setEndOffset(start + newAnnotation.getQuote().length());
                            range.setStart("");
                            range.setEnd("");
                            newAnnotation.setRange(range);
                            newAnnotation.setUri(uri + startTime);

                            LocalDateTime localDateTime = LocalDateTime.now();
                            newAnnotation.setCreated(localDateTime);
                            newAnnotation.setUpdated(localDateTime);
                            newAnnotation.setUser(userService.findByUsername("maxsibilla"));

                            if (preAnnotationType.equals("english")) {
                                newAnnotation.setTags(new String[]{"english"});
                                newAnnotation.setWordType("english");
                                newAnnotation.setWordDifficulty(englishDefinitions.get(newAnnotation.getQuote()).getDifficulty());
                            } else {
                                newAnnotation.setWordType("scientific");
                                newAnnotation.setTags(new String[]{"scientific"});
                            }

                            annotationService.save(newAnnotation);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildAcceptedSemanticTypesList(List<String> list) throws FileNotFoundException {
        Scanner s = new Scanner(new java.io.File(ANNOTATOR_DIR + "pre-annotation/t1.txt"));
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
    }

    private void getPhrasesFromJson(Map<String, String> phrases, List<String> acceptedSemanticTypes, List<String[]> reportData, String preAnnotationFile) throws IOException {
        String json = new String(java.nio.file.Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "pre-annotation/" + preAnnotationFile)));
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

    private void getPhrasesFromEnglishWordListing(Map<String, String> phrases, List<String[]> reportData, String preAnnotationFile) throws IOException {
        String htmlString = new String(java.nio.file.Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "pre-annotation/" + preAnnotationFile))).toLowerCase();
        for (Map.Entry<String, EnglishPhrase> entry : englishDefinitions.entrySet()) {
            if (isContain(htmlString, entry.getKey())) {
                if (entry.getValue().getDifficulty().equals("D") || entry.getValue().getDifficulty().equals("T6")) {
                    phrases.put(entry.getKey(), entry.getValue().getDefinition());
                    reportData.add(new String[]{entry.getKey(), entry.getValue().getDefinition(), entry.getValue().getDifficulty()});
                }
            }
        }
    }

    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    private void serializeDefinitions() {
        Map<String, String> map = new HashMap<>();
        String csvFile = "/Users/mas400/dev/annotator-file-dir/pre-annotation/select_CUI__DEF_from_MRDEF_where_SAB____.csv";
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

            FileOutputStream fos = new FileOutputStream(ANNOTATOR_DIR + "pre-annotation/definitions.ser");
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
            java.io.File csvOutputFile = new java.io.File(ANNOTATOR_DIR + "pre-annotation/report.csv");
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
            FileInputStream fis = new FileInputStream(ANNOTATOR_DIR + "pre-annotation/definitions.ser");
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
        String csvFile = "/Users/mas400/dev/annotator-file-dir/pre-annotation/Andy Biemiller Words Worth Teaching Alphabetical and Ratings List.csv";
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

            FileOutputStream fos = new FileOutputStream(ANNOTATOR_DIR + "pre-annotation/englishDefinitions.ser");
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
            FileInputStream fis = new FileInputStream(ANNOTATOR_DIR + "pre-annotation/englishDefinitions.ser");
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

    private void createAnnotations(List<Annotation> annotations, String annotationFile) {
        if (definitions == null) {
            definitions = deserializeDefinitions();
        }
        if (englishDefinitions == null) {
            englishDefinitions = deserializeEnglishDefinitions();
        }

        String csvFile = ANNOTATOR_DIR + "pre-annotation/" + annotationFile;

        try (
                Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "utf-8"));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withHeader("word", "definition", "video", "image", "difficulty")
                        .withIgnoreHeaderCase()
                        .withSkipHeaderRecord()
                        .withTrim());) {

            for (CSVRecord csvRecord : csvParser) {
                Annotation annotation = new Annotation();
                annotation.setQuote(csvRecord.get("word"));
                annotation.setText(csvRecord.get("definition"));
                annotation.setVideo(csvRecord.get("video"));
                annotation.setFigure(csvRecord.get("image"));

                annotations.add(annotation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUserFileInformation() {
        try {
            String json = new String(java.nio.file.Files.readAllBytes(Paths.get(ANNOTATOR_DIR + "usersConfig.json")));
            JsonArray usersJson = new JsonParser().parse(json).getAsJsonObject().get("users").getAsJsonArray();
            for (int i = 0; i < usersJson.size(); i++) {
                String username = usersJson.get(i).getAsJsonObject().keySet().iterator().next();
                JsonArray userFiles = usersJson.get(i).getAsJsonObject().get(username).getAsJsonObject().get("files").getAsJsonArray();
                List<UserFileInfo> userFileInfoList = new ArrayList<>();
                for (int j = 0; j < userFiles.size(); j++) {
                    UserFileInfo userFileInfo = new UserFileInfo();
                    JsonObject fileObject = userFiles.get(j).getAsJsonObject();
                    userFileInfo.setFilename(fileObject.get("name").getAsString());
                    String displayName = fileService.findByUri(fileObject.get("name").getAsString()).getDisplayName();
                    userFileInfo.setPublicFilename(displayName);
                    userFileInfo.setPreTest(fileObject.get("preTest").getAsString());
                    userFileInfo.setPostTest(fileObject.get("postTest").getAsString());
                    userFileInfo.setHasAssistance(fileObject.get("hasAssistance").getAsBoolean());
                    userFileInfoList.add(userFileInfo);
                }
                userFileInformation.put(username, userFileInfoList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class UserFileInfo {
        private String filename;
        private String publicFilename;
        private String preTest;
        private String postTest;
        private boolean hasAssistance;

        public String getFilename() {
            return filename;
        }

        public String getPublicFilename() {
            return publicFilename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void setPublicFilename(String publicFilename) {
            this.publicFilename = publicFilename;
        }

        public boolean isHasAssistance() {
            return hasAssistance;
        }

        public void setHasAssistance(boolean hasAssistance) {
            this.hasAssistance = hasAssistance;
        }

        public String getPreTest() {
            return preTest;
        }

        public void setPreTest(String preTest) {
            this.preTest = preTest;
        }

        public String getPostTest() {
            return postTest;
        }

        public void setPostTest(String postTest) {
            this.postTest = postTest;
        }
    }
}
