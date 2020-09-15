package com.example.Julien3DBack.UploadFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class UploadImageController {

    @Autowired
    UploadImageRepository uploadImageRepository;

    @PostMapping("/upload")
    public void uplaodImage(@RequestParam("imageFile") MultipartFile file, @RequestBody UploadImage uploadImage) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        UploadImage img = new UploadImage(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        uploadImageRepository.save(img);
    }

    @GetMapping(path = { "/get/{name}" })
    public UploadImage getImage(@PathVariable("name") String imageName) throws IOException {

        final Optional<UploadImage> retrievedImage = uploadImageRepository.findByName(imageName);
        UploadImage img = new UploadImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
