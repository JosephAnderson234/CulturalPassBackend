package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.Event.exceptions.BadImageUploadException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

@Service
public class ImageService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SupabaseClient supabaseClient;

    @Transactional
    public String uploadEventImage(Long eventId, MultipartFile imageFile){
        try{
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
            if (event.getImageUrl() != null && !event.getImageUrl().isBlank()) {
                String oldFileName = event.getImageUrl().substring(event.getImageUrl().lastIndexOf("/") + 1);
                supabaseClient.deleteImage(oldFileName);
                event.setImageUrl(null);
            }
            byte[] webpImageBytes = convertToWebP(imageFile);
            String fileName = "event_" + eventId + "_" + System.currentTimeMillis() + ".webp";
            String publicUrl = supabaseClient.uploadImage(webpImageBytes, fileName);
            event.setImageUrl(publicUrl);
            eventRepository.save(event);
            return publicUrl;
        } catch (Exception e) {
            throw new BadImageUploadException("No se proporcionó una imagen", e);
        }
    }

    private byte[] convertToWebP(MultipartFile imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        if (originalImage == null) {
            throw new IOException("No se pudo leer la imagen. Formato no soportado.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean success = ImageIO.write(originalImage, "webp", baos);
        if (!success) {
            throw new IOException("No se pudo convertir la imagen a formato WebP");
        }
        return baos.toByteArray();
    }
}
