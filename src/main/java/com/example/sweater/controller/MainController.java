package com.example.sweater.controller;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}") // выдергивает из контекста, а точнее из проперти имя строки и вставляет его в переменную
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main"; // выдача наших сообщений
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam("file")MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);

        if (!file.isEmpty()){ // будем сохранять файл только если у него заданно имя файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){ // если uploadDir не существует
                uploadDir.mkdirs(); // то мы ее создаем
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename(); // делаем уникальное имя файла

            file.transferTo(new File(uploadPath + "/" + resultFilename)); // загружаем файл

            message.setFilename(resultFilename);
        }

        messageRepository.save(message);
//      Сохраняем сообщения которые вводяться в репозиторий
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);
//      Взяли из репозитория положили в модель и отдали пользователю

        return "main";
    }

}
