package ch.avocado.share.service.Mock;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.data.File;
import ch.avocado.share.model.exceptions.ServiceNotFoundException;
import ch.avocado.share.service.IFileDataHandler;

/**
 * Created by bergm on 19/03/2016.
 */
public class FileDataHandlerMock extends DataHandlerMockBase<File> implements IFileDataHandler {


    @Override
    public String addFile(File file) {
        if(file.getTitle() == null) throw new IllegalArgumentException("file.title is null");
        if(file.getModuleId() == null) throw new IllegalArgumentException("file.moduleId is null");
        if(file.getPath() == null) throw new IllegalArgumentException("file.path is null");
        if(file.getLastChanged() == null) throw new IllegalArgumentException("file.lastChanged is null");
        return add(file);
    }

    @Override
    public boolean deleteFile(File file) {
        return delete(file);
    }

    @Override
    public File getFileById(String fileId) {
        return get(fileId);
    }

    @Override
    public File getFileByTitleAndModule(String fileTitle, String moduleId) {
        if(fileTitle == null) throw new IllegalArgumentException("fileTitle is null");
        if(moduleId == null) throw new IllegalArgumentException("moduleId is null");
        for(File file: objects.values()) {
            if(fileTitle.equals(file.getTitle()) && moduleId.equals(file.getModuleId())) {
                return file;
            }
        }
        return null;
    }

    @Override
    public boolean updateFile(File file) {
        return update(file);
    }

    @Override
    public boolean grantAccess(String fileId, String userId) {
        return false;
    }

    public static void use() throws Exception {
        if(!ServiceLocator.getService(IFileDataHandler.class).getClass().equals(FileDataHandlerMock.class)) {
            ServiceLocatorModifier.setService(IFileDataHandler.class, new FileDataHandlerMock());
        }
    }
}
