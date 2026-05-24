package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting WebP images to JPG format
 * since JavaFX doesn't natively support WebP
 */
public class ImageConverter {
    
    private static final String DWEBP_PATH = System.getenv("TEMP") + "\\libwebp\\libwebp-1.4.0-windows-x64\\bin\\dwebp.exe";
    private static boolean converterAvailable = false;
    
    static {
        // Check if dwebp converter is available
        File dwebpFile = new File(DWEBP_PATH);
        converterAvailable = dwebpFile.exists();
        if (!converterAvailable) {
            System.out.println("WebP converter not available at: " + DWEBP_PATH);
            System.out.println("WebP images will not be displayed.");
        }
    }
    
    /**
     * Automatically converts all WebP files in a folder to JPG
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
        File[] webpFiles = folder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".webp"));
        
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
                    jpgPath
                );
                
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
