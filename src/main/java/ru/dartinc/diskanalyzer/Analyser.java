package ru.dartinc.diskanalyzer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Analyser {

    private Map<String, Long> sizes;

    public Map<String, Long> calculateDirectorySize(Path path) {
        sizes = new HashMap<>();
        try {
            Files.walkFileTree(
                    path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (Files.isRegularFile(file) || Files.isDirectory(file)) {
                                long size = Files.size(file);
                                updateDirSize(file, size);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    }

            );
            return sizes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDirSize(Path path, Long size) {
        String key = path.toString();
        sizes.put(key, size + sizes.getOrDefault(key, 0L));
        Path parent = path.getParent();
        if (parent != null) {
            updateDirSize(parent, size);
        }
    }

}
