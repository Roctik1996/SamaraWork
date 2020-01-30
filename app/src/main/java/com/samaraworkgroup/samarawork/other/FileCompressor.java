package com.samaraworkgroup.samarawork.other;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public class FileCompressor {
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private String destinationDirectoryPath;

    public FileCompressor(Context context) {
        destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
    }


    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    private File compressToFile(File imageFile, String compressedFileName) throws IOException {
        //max width and height values of the compressed image is taken as 612x816
        int maxWidth = 612;
        int maxHeight = 816;
        int quality = 80;
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }
}
