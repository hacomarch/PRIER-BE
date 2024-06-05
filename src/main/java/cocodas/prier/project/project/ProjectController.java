package cocodas.prier.project.project;

import cocodas.prier.project.project.dto.ProjectForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    @PostMapping
    public ResponseEntity<String> createProject(@RequestPart("form") ProjectForm form,
                                                @RequestParam("mainImage") MultipartFile mainImage,
                                                @RequestParam("contentImages") MultipartFile[] contentImages,
                                                @RequestHeader("Authorization") String auth) {

        String token = getToken(auth);
        String result = projectService.createProject(form, mainImage, contentImages, token);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        String result = projectService.deleteProject(projectId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId,
                                                @RequestPart("form") ProjectForm form,
                                                @RequestParam("mainImage") MultipartFile mainImage,
                                                @RequestParam("contentImages") MultipartFile[] contentImages) {

        String result = projectService.updateProject(projectId, form, mainImage, contentImages);
        return ResponseEntity.ok(result);
    }

}
