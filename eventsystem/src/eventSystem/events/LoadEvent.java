package eventSystem.events;

import java.util.EventObject;

public class LoadEvent extends EventObject {
    private final String fileType;

    public LoadEvent(String fileType) {
        super(fileType);
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
