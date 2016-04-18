package ch.avocado.share.model.data;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.exceptions.ServiceNotFoundException;
import ch.avocado.share.service.IFileDataHandler;
import ch.avocado.share.service.exceptions.DataHandlerException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Module model
 */
public class Module extends AccessControlObjectBase {

    private String name;
    private List<String> fileIds;

    public Module(String id, List<Category> categories, Date creationDate, float rating, String ownerId, String description, String name, List<String> fileIds) {
        super(id, categories, creationDate, rating, ownerId, description);
        setName(name);
        setFileIds(fileIds);
    }

    public Module(String ownerId, String description, String name) {
        this(null, new ArrayList<>(), new Date(), 0.0f, ownerId, description, name, new ArrayList<>());
    }

    /**
     * @return The name of the module
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the module
     */
    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null");
        this.name = name;
    }


    @Override
    public String getReadableName() {
        return getName();
    }

    public void setFileIds(List<String> fileIds) {
        if(fileIds == null) throw new IllegalArgumentException("fileIds is null");
        this.fileIds = new ArrayList<>(fileIds);
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public List<File> getFiles() throws DataHandlerException, ServiceNotFoundException {
        if(fileIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            IFileDataHandler fileDataHandler = ServiceLocator.getService(IFileDataHandler.class);
            return fileDataHandler.getFiles(this.fileIds);
        }
    }
}