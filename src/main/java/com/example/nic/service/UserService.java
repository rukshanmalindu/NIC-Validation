package com.example.nic.service;


import com.example.nic.dto.UserDTO;
import com.example.nic.entity.User;
import com.example.nic.repo.UserRepo;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


public void processAndSaveNICFiles(List<MultipartFile> files) {
    for (MultipartFile file : files) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isValidNIC(line)) {
                    User user = extractUser(line);
                    user.setFname(file.getOriginalFilename());
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userRepo.saveAll(users);
    }
}

    public List<UserDTO> getAllValidNICs() {
        List<User> users = userRepo.findAll();
        List<UserDTO> dtoList = new ArrayList<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setNicNumber(user.getNicNumber());
            dto.setGender(user.getGender());
            dto.setDob(user.getDob());
            dto.setAge(user.getAge());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private boolean isValidNIC(String nic) {
        return nic.matches("\\d{9}[Vv]|\\d{12}");
    }

    private User extractUser(String nic) {
        User user = new User();

        String birthYear;
        int days;
        if (nic.length() == 10) { // Old NIC format
            birthYear = "19" + nic.substring(0, 2);
            days = Integer.parseInt(nic.substring(2, 5));
        } else { // New NIC format
            birthYear = nic.substring(0, 4);
            days = Integer.parseInt(nic.substring(4, 7));
        }

        String gender = (days > 500) ? "Female" : "Male";
        if (days > 500) days -= 500;

        LocalDate birthday = LocalDate.of(Integer.parseInt(birthYear), 1, 1).plusDays(days - 1);
        int age = Period.between(birthday, LocalDate.now()).getYears();

        user.setNicNumber(nic);
        user.setGender(gender);
        user.setDob(birthday.toString());
        user.setAge(age);

        return user;
    }
    public UserDTO getNICSummary() {
        UserDTO summaryDTO = new UserDTO();

        long totalRecords = userRepo.count();
        long maleUsers = userRepo.countByGender("Male");
        long femaleUsers = userRepo.countByGender("Female");

        summaryDTO.setTotalRecords(totalRecords);
        summaryDTO.setMaleUsers(maleUsers);
        summaryDTO.setFemaleUsers(femaleUsers);

        return summaryDTO;
    }

    // PDF Generation
//    public ByteArrayInputStream generateNICRecordsPDF() {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            PdfWriter writer = new PdfWriter(out);
//            PdfDocument pdfDoc = new PdfDocument(writer);
//            Document document = new Document(pdfDoc);
//
//            document.add(new Paragraph("NIC Validation System Report"));
//
//            Table table = new Table(new float[]{4, 4, 4, 4});
//            table.addCell("NIC Number");
//            table.addCell("Date of Birth");
//            table.addCell("Age");
//            table.addCell("Gender");
//
//            List<User> users = userRepo.findAll();
//            for (User user : users) {
//                table.addCell(user.getNicNumber());
//                table.addCell(user.getDob());
//                table.addCell(String.valueOf(user.getAge()));
//                table.addCell(user.getGender());
//            }
//
//            document.add(table);
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new ByteArrayInputStream(out.toByteArray());
//    }

    // CSV Generation
    public ByteArrayInputStream generateNICRecordsCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("NIC Number,Date of Birth,Age,Gender\n");

        List<User> users = userRepo.findAll();
        for (User user : users) {
            sb.append(user.getNicNumber()).append(",");
            sb.append(user.getDob()).append(",");
            sb.append(user.getAge()).append(",");
            sb.append(user.getGender()).append("\n");
        }

        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    // Excel Generation
    public ByteArrayInputStream generateNICRecordsExcel() {
        // Implement Excel generation logic here
        // Return a ByteArrayInputStream
        return new ByteArrayInputStream(new byte[0]); // Placeholder for Excel logic
    }
}







