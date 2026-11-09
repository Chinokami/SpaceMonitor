package com.devda.spacemonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Service
public class ImageDatasetService {
    private final Path datasetDirectory;

    public ImageDatasetService(
            @Value("${app.images.dataset-directory:./data/images}") String directory) {
        this.datasetDirectory = Paths.get(directory).toAbsolutePath().normalize();
    }

    public String pickRandomImageUrl() {
        if (!Files.isDirectory(datasetDirectory)) return null;

        try (Stream<Path> paths = Files.list(datasetDirectory)) {
            List<Path> images = paths.filter(Files::isRegularFile)
                    .filter(this::isSupportedImage).toList();
            if (images.isEmpty()) return null;

            Path selected = images.get(ThreadLocalRandom.current().nextInt(images.size()));
            return "/media/" + selected.getFileName();
        } catch (IOException exception) {
            return null;
        }
    }

    private boolean isSupportedImage(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg")
                || name.endsWith(".jpeg") || name.endsWith(".webp");
    }

    public Path getDatasetDirectory() { return datasetDirectory; }
}
