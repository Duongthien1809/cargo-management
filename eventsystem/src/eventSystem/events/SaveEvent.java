package eventSystem.events;

import java.util.EventObject;

public class SaveEvent extends EventObject {
    private final String fileType;

    public SaveEvent(String fileType) {
        super(fileType);
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
