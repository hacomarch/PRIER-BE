package cocodas.prier.project.project;

import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import cocodas.prier.project.feedback.response.ResponseService;
import cocodas.prier.project.project.dto.ProjectDetailDto;
import cocodas.prier.project.project.dto.ProjectDto;
import cocodas.prier.project.project.dto.ProjectForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId,
                                                @RequestHeader("Authorization") String auth) {

        String token = getToken(auth);
        String result = projectService.deleteProject(token, projectId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId,
                                                @RequestPart("form") ProjectForm form,
                                                @RequestParam("mainImage") MultipartFile mainImage,
                                                @RequestParam("contentImages") MultipartFile[] contentImages,
                                                @RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        String result = projectService.updateProject(projectId, form, mainImage, contentImages, token);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailDto> getProjectDetail(@PathVariable Long projectId) {
        ProjectDetailDto result = projectService.getProjectDetail(projectId);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getProjects(@RequestParam(value = "search", required = false) String keyword) {
        List<ProjectDto> projects;
        if (keyword != null && !keyword.isEmpty()) {
            projects = projectService.getSearchedProjects(keyword);
        } else {
            projects = projectService.getAllProjects();
        }

        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}/extend")
    public ResponseEntity<String> extendFeedback(@PathVariable Long projectId,
                                                 @RequestParam Integer weeks,
                                                 @RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        String result = projectService.extendFeedback(projectId, weeks, token);

        return ResponseEntity.ok(result);
    }

}
