package Util;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class for converting WebP images to JPG format
 * since JavaFX doesn't natively support WebP
 */
public class ImageConverter {

    private static final String LIBWEBP_VERSION = "libwebp-1.4.0-windows-x64";
    private static final String DOWNLOAD_URL = "https://storage.googleapis.com/downloads.webmproject.org/releases/webp/"
            + LIBWEBP_VERSION + ".zip";
    private static final String DWEBP_PATH = System.getenv("TEMP") + "\\libwebp\\" + LIBWEBP_VERSION
            + "\\bin\\dwebp.exe";
    private static boolean converterAvailable = false;

    static {
        File dwebpFile = new File(DWEBP_PATH);
        if (dwebpFile.exists()) {
            converterAvailable = true;
        } else {
            System.out.println("WebP converter not found. Attempting to download libwebp automatically...");
            converterAvailable = downloadAndExtractLibwebp();
        }
        if (!converterAvailable) {
            System.out.println("WebP images will not be displayed.");
        }
    }

    private static boolean downloadAndExtractLibwebp() {
        try {
            Path libwebpDir = Paths.get(System.getenv("TEMP"), "libwebp");
            Files.createDirectories(libwebpDir);
            Path zipPath = libwebpDir.resolve(LIBWEBP_VERSION + ".zip");

            System.out.println("Downloading: " + DOWNLOAD_URL);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DOWNLOAD_URL))
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                System.err.println("Download failed with HTTP status: " + response.statusCode());
                return false;
            }

            try (InputStream in = response.body();
                    OutputStream out = Files.newOutputStream(zipPath)) {
                in.transferTo(out);
            }
            System.out.println("Download complete. Extracting...");

            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path outPath = libwebpDir.resolve(entry.getName());
                    if (entry.isDirectory()) {
                        Files.createDirectories(outPath);
                    } else {
                        Files.createDirectories(outPath.getParent());
                        Files.copy(zis, outPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                    zis.closeEntry();
                }
            }

            Files.deleteIfExists(zipPath);

            if (new File(DWEBP_PATH).exists()) {
                System.out.println("libwebp installed successfully.");
                return true;
            } else {
                System.err.println("Extraction completed but dwebp.exe not found at expected path.");
                return false;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to auto-install libwebp: " + e.getMessage());
            return false;
        }
    }

    /**
     * Automatically converts all WebP files in a folder to JPG
     * 
     * @param folderPath Path to the folder containing images
     * @return Number of files converted
     */
    public static int convertWebPFilesInFolder(String folderPath) {
        if (!converterAvailable) {
            return 0;
        }

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return 0;
        }

        // Find all WebP files
        File[] webpFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".webp"));

        if (webpFiles == null || webpFiles.length == 0) {
            return 0;
        }

        int convertedCount = 0;
        List<String> commands = new ArrayList<>();

        for (File webpFile : webpFiles) {
            String jpgPath = webpFile.getAbsolutePath().replaceAll("\\.webp$", ".jpg");
            File jpgFile = new File(jpgPath);

            // Skip conversion if JPG already exists, but delete the WebP
            if (jpgFile.exists()) {
                if (webpFile.delete()) {
                    System.out.println("Deleted redundant WebP (JPG exists): " + webpFile.getName());
                }
                continue;
            }

            try {
                // Build command: dwebp input.webp -o output.jpg
                ProcessBuilder pb = new ProcessBuilder(
                        DWEBP_PATH,
                        webpFile.getAbsolutePath(),
                        "-o",
                        jpgPath);

                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode == 0 && jpgFile.exists()) {
                    convertedCount++;
                    System.out.println("Converted: " + webpFile.getName() + " -> " + jpgFile.getName());

                    // Delete the original WebP file after successful conversion
                    if (webpFile.delete()) {
                        System.out.println("Deleted original WebP: " + webpFile.getName());
                    } else {
                        System.err.println("Warning: Could not delete WebP file: " + webpFile.getName());
                    }
                } else {
                    System.err.println("Failed to convert: " + webpFile.getName());
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error converting " + webpFile.getName() + ": " + e.getMessage());
            }
        }

        return convertedCount;
    }

    /**
     * Converts all WebP files in all listing folders
     * 
     * @return Total number of files converted
     */
    public static int convertAllListingImages() {
        File rootFolder = new File("room_rental_img");
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return 0;
        }

        int totalConverted = 0;
        File[] subFolders = rootFolder.listFiles(File::isDirectory);

        if (subFolders != null) {
            for (File subFolder : subFolders) {
                int converted = convertWebPFilesInFolder(subFolder.getAbsolutePath());
                if (converted > 0) {
                    System.out.println("Converted " + converted + " images in: " + subFolder.getName());
                }
                totalConverted += converted;
            }
        }

        return totalConverted;
    }

    /**
     * Check if the WebP converter is available
     */
    public static boolean isConverterAvailable() {
        return converterAvailable;
    }
}
