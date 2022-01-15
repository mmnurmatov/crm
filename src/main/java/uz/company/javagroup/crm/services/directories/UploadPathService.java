package uz.isd.javagroup.grandcrm.services.directories;

import java.io.File;

public interface UploadPathService {

    File getFilePath(String modifiedFileName, String path);
}
