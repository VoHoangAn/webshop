package com.example.webshop.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {
    private final Path root = Paths.get("webshop/upload");
    private final Cloudinary cloudinary;

    public Map upload(String urlImg) throws IOException {
        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true,
                "folder","webshop"
        );

        return cloudinary.uploader().upload(this.root.resolve(urlImg).toFile(),params1);
    }

}
