package edu.pitt.nccih.controller;

import edu.pitt.nccih.GlobalConstants;
import edu.pitt.nccih.auth.model.User;
import edu.pitt.nccih.auth.service.UserService;
import edu.pitt.nccih.models.Annotation;
import edu.pitt.nccih.models.AnnotationTracker;
import edu.pitt.nccih.models.SearchResult;
import edu.pitt.nccih.repository.AnnotationTrackerRepository;
import edu.pitt.nccih.service.AnnotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/annotation")
public class AnnotatorController {
    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private AnnotationTrackerRepository annotationTrackerRepository;

    @Autowired
    private UserService userService;

    static final Logger logger = LoggerFactory.getLogger(AnnotatorController.class);
    public static final String ANNOTATOR_DIR;

    static {
        Map<String, String> env = System.getenv();
        String annotatorDir = env.get(GlobalConstants.ANNOTATOR_FILEDIR_ENVIRONMENT_VARIABLE);
        if (annotatorDir != null) {
            if (!annotatorDir.endsWith(File.separator)) {
                annotatorDir += File.separator;
            }
            logger.info(GlobalConstants.ANNOTATOR_FILEDIR_ENVIRONMENT_VARIABLE + " is now:" + annotatorDir);
        } else {
            logger.error(GlobalConstants.ANNOTATOR_FILEDIR_ENVIRONMENT_VARIABLE + "environment variable not found!");
        }

        ANNOTATOR_DIR = annotatorDir;
    }


    @GetMapping("/annotations/{id}")
    @ResponseBody
    public Annotation retrieve(@PathVariable String id) {
        return annotationService.findById(Long.parseLong(id));
    }

    @RequestMapping(value = "/annotations", method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response) {
        response.setStatus(204);
    }

    @RequestMapping(value = "/trackAnnotation", method = RequestMethod.POST)
    @ResponseBody
    public void trackAnnotation(@RequestParam int annotationId, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());
            Annotation annotation = annotationService.findById(Long.valueOf(annotationId));
            AnnotationTracker annotationTracker = new AnnotationTracker();
            annotationTracker.setUser(user);
            annotationTracker.setAnnotation(annotation);

            annotationTrackerRepository.save(annotationTracker);

            return;
        }
        response.setStatus(401);

    }


    @RequestMapping(value = "/newAnnotation", method = RequestMethod.POST)
    @ResponseBody
    public void createProgrammatically(@RequestBody Annotation annotation, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());

            if (annotation.getWordType().equals("english")) {
                annotation.setTags(new String[]{"english"});
                annotation.setWordDifficulty(HomeController.englishDefinitions.get(annotation.getQuote()).getDifficulty());
            } else {
                annotation.setTags(new String[]{"scientific"});
            }


            if (annotation.getId() == null) {
                LocalDateTime localDateTime = LocalDateTime.now();
                annotation.setCreated(localDateTime);
                annotation.setUpdated(localDateTime);
                annotation.setUser(user);
            }
            try {
                annotationService.save(annotation);
                response.setStatus(204);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(500);
                return;
            }
        }
//        access denied
        response.setStatus(401);
    }

    @RequestMapping(value = "/annotations", method = RequestMethod.POST)
    public void create(@RequestBody Annotation annotation, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            User user = userService.findByUsername(session.getAttribute("username").toString());

            if (annotation.getId() == null) {
                LocalDateTime localDateTime = LocalDateTime.now();
                annotation.setCreated(localDateTime);
                annotation.setUpdated(localDateTime);
                annotation.setUser(user);
            }
            try {
                annotationService.save(annotation);
                response.setStatus(204);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(500);
                return;
            }
        }
        //access denied
        response.setStatus(401);
    }

    @GetMapping("/search")
    @ResponseBody
    public SearchResult search(@RequestParam(required = false) String text,
                               @RequestParam(required = false) Integer limit,
                               @RequestParam(required = false) String uri) {
        SearchResult searchResult = new SearchResult();
        searchResult.setRows((List<Annotation>) annotationService.findByUri(uri));
        if (searchResult.getRows() != null) {
            searchResult.setTotal(searchResult.getRows().size());
        } else {
            searchResult.setTotal(0);
        }
        return searchResult;
    }

    @RequestMapping(value = "/annotations/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable String id, @RequestBody Annotation annotation, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            Annotation oldAnnotation = annotationService.findById(Long.parseLong(id));
            oldAnnotation.setText(annotation.getText());
            oldAnnotation.setQuote(annotation.getQuote());
            oldAnnotation.setUpdated(LocalDateTime.now());
            try {
                annotationService.save(oldAnnotation);
                response.setStatus(204);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(500);
                return;
            }
        }
        //access denied
        response.setStatus(401);
    }

    @DeleteMapping("/annotations/{id}")
    @ResponseBody
    public void delete(@PathVariable String id, HttpSession session, HttpServletResponse response) {
        if (Interceptor.ifLoggedIn(session)) {
            try {
                annotationService.delete(Long.parseLong(id));
                response.setStatus(204);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(500);
                return;
            }
        }
        //access denied
        response.setStatus(401);
    }
}
