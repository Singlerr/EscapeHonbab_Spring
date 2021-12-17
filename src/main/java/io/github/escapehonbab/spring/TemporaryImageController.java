package io.github.escapehonbab.spring;

import io.github.escapehonbab.spring.jwt.errors.ImageNotFoundException;
import io.github.escapehonbab.spring.service.TemporaryImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/img")
public class TemporaryImageController {

    private TemporaryImageService service;

    @GetMapping("/{hash}")
    public @ResponseBody
    String getImage(@PathVariable String hash) throws ImageNotFoundException {
        return Base64.getEncoder().encodeToString(service.getImage(hash).orElseThrow(() -> new ImageNotFoundException("Image not Found")));
    }
}
