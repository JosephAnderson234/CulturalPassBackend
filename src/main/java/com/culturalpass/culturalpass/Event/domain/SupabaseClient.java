package com.culturalpass.culturalpass.Event.domain;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SupabaseClient {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    public String uploadImage(byte[] imageData, String fileName) throws Exception {
        String url = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;
        Request request = Request.put(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .bodyByteArray(imageData, ContentType.IMAGE_JPEG);
        request.execute().returnContent();
        return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;
    }

    public void deleteImage(String fileName) throws Exception {
        String url = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;
        Request request = Request.delete(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey);
        request.execute();
    }
}
