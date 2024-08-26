package eventSystem.events;

import administration.Storable;
import cargo.Cargo;

import java.util.EventObject;

public class ShowHazardsEvent<T extends Cargo & Storable> extends EventObject {
    private final boolean containsHazards;

    public ShowHazardsEvent(boolean containsHazards) {
        super(containsHazards);
        this.containsHazards = containsHazards;
    }

    public boolean containsHazards() {
        return containsHazards;
    }
}
