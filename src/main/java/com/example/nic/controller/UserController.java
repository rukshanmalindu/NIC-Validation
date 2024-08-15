package com.example.nic.controller;


import com.example.nic.dto.UserDTO;
import com.example.nic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/manage")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/upload-csv")

    public ResponseEntity<Map<String, String>> uploadNICFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            userService.processAndSaveNICFiles(files);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Files uploaded and processed successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to process files");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/api/nics")
    public ResponseEntity<List<UserDTO>> getAllValidNICs() {
        List<UserDTO> nicInfoDTOs = userService.getAllValidNICs();
        return ResponseEntity.ok(nicInfoDTOs);
    }

    // Summery data
    @GetMapping("/api/nicSummary")
    public ResponseEntity<UserDTO> getNICSummary() {
        UserDTO summaryDTO = userService.getNICSummary();
        return ResponseEntity.ok(summaryDTO);
    }

    @GetMapping("/download/{format}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable String format) {
        ByteArrayInputStream bis;
        String filename;
        HttpHeaders headers = new HttpHeaders();

        switch (format.toLowerCase()) {
//            case "pdf":
//                bis = userService.generateNICRecordsPDF();
//                filename = "NIC_Records_Report.pdf";
//                headers.add("Content-Disposition", "inline; filename=" + filename);
//                return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
            case "csv":
                bis = userService.generateNICRecordsCSV();
                filename = "NIC_Records_Report.csv";
                headers.add("Content-Disposition", "attachment; filename=" + filename);
                return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/csv")).body(new InputStreamResource(bis));
            case "excel":
                bis = userService.generateNICRecordsExcel();
                filename = "NIC_Records_Report.xlsx";
                headers.add("Content-Disposition", "attachment; filename=" + filename);
                return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(bis));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}



